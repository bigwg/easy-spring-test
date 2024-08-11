package com.github.bigwg.easy.spring.test.context;

import com.github.bigwg.easy.spring.test.annotation.AutoImport;
import com.github.bigwg.easy.spring.test.util.BeanDefinitionRegistryUtil;
import com.github.bigwg.easy.spring.test.util.ClassFieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AutoImportsContextCustomizer
 *
 * @author zhaozhiwei
 * @since 2022/3/3
 */
@Slf4j
public class AutoImportsContextCustomizer implements ContextCustomizer {

    private AutoImport annotation;

    private final Class<?> testClass;

    private ConfigurableApplicationContext context;

    /**
     * 需要 mock 的类集合
     */
    private final Set<Class<?>> mockBeans = new HashSet<>();

    /**
     * 已扫描过的类集合
     */
    private final Set<FieldClass> scannedBeans = new HashSet<>();

    /**
     * 待生成 spring bean 的类集合
     */
    private final Set<FieldClass> candidateBeans = new HashSet<>();

    /**
     * 每次递归扫描抽象类或接口集合，需要在包扫描阶段判断是否需要生成对应实例
     */
    private Set<FieldClass> needScanBeansClass = new HashSet<>();

    private final Map<String, BeanDefinition> beanDefinitionCache = new HashMap<>();

    /**
     * 自动 mock 测试控制器集合
     */
    private final List<AutoMockTestController> autoMockTestControllers = new LinkedList<>();

    /**
     * 自动注入字段判断器（包含自动注入能力的自定义注解）
     */
    private final List<FieldScanJudge> fieldScanJudges = new LinkedList<>();

    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
            new LinkedHashSet<Class<? extends Annotation>>(4);


    AutoImportsContextCustomizer(Class<?> testClass) {
        this.testClass = testClass;
        autowiredAnnotationTypes.add(Autowired.class);
        autowiredAnnotationTypes.add(Resource.class);
    }

    @Override
    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        AutoImport annotation = AnnotationUtils.findAnnotation(testClass, AutoImport.class);
        if (Objects.isNull(annotation)) {
            return;
        }
        this.context = context;
        this.annotation = annotation;
        try {
            // 扫描待 mock bean
            scanMockBeans();
            // 初始化自动 mock 控制器
            initAutoMockController();
            // 初始化字段扫描判断器
            initFieldScanJudges();
            // 扫描待处理 bean
            scanAndSearchBeans();
            // 执行自动 mock
            executeAutoMock();
            // 注册待生成 bean definition
            registerBeanDefinitions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void executeAutoMock() {
        for (AutoMockTestController autoMockTestController : autoMockTestControllers) {
            autoMockTestController.autoMock();
        }
    }

    private void registerBeanDefinitions() {
        BeanDefinitionRegistry beanDefinitionRegistry = BeanDefinitionRegistryUtil.getBeanDefinitionRegistry(context);
        AnnotationBeanNameGenerator annotationBeanNameGenerator = new AnnotationBeanNameGenerator();
        for (FieldClass finalImportBean : candidateBeans) {
            Class<?> clazz = finalImportBean.getClazz();
            BeanDefinition importBeanDefinition = beanDefinitionCache.get(clazz.getName());
            AnnotatedBeanDefinition annotatedBeanDefinition;
            if (Objects.isNull(importBeanDefinition)
                    || !(importBeanDefinition instanceof AnnotatedBeanDefinition)) {
                annotatedBeanDefinition = new AnnotatedGenericBeanDefinition(clazz);
            } else {
                annotatedBeanDefinition = (AnnotatedBeanDefinition) importBeanDefinition;
            }
            String beanName = annotationBeanNameGenerator.generateBeanName(annotatedBeanDefinition, beanDefinitionRegistry);
            beanDefinitionRegistry.registerBeanDefinition(beanName, annotatedBeanDefinition);
        }
    }

    private void scanAndSearchBeans() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException,
            ClassNotFoundException {
        BeanDefinitionRegistry beanDefinitionRegistry = BeanDefinitionRegistryUtil.getBeanDefinitionRegistry(context);
        List<FieldClass> entryClasses = new LinkedList<>();
        // 填充指定的扫描入口类
        if (Objects.nonNull(annotation.value()) && annotation.value().length > 0) {
            entryClasses.addAll(Arrays.stream(annotation.value()).map(FieldClass::buildNoField).collect(Collectors.toList()));
        }
        // 填充默认扫描入口类（测试类依赖注入的类）
        entryClasses.add(FieldClass.buildNoField(testClass));
        String[] scanBasePackages = annotation.scanBasePackages();
        // 指定类扫描
        if (!entryClasses.isEmpty()) {
            for (FieldClass entryClass : entryClasses) {
                searchNeedImportBeans(entryClass);
            }
        }
        // 包路径扫描
        if (scanBasePackages.length == 0) {
            return;
        }
        ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry, true);
        Set<BeanDefinition> scanBeanDefinitions = new LinkedHashSet<>();
        for (String scanBasePackage : scanBasePackages) {
            if (!StringUtils.isEmpty(scanBasePackage)) {
                scanBeanDefinitions.addAll(classPathBeanDefinitionScanner.findCandidateComponents(scanBasePackage));
            }
        }
        if (CollectionUtils.isEmpty(scanBeanDefinitions)) {
            return;
        }
        while (!CollectionUtils.isEmpty(needScanBeansClass)) {
            Set<FieldClass> needScanBeansClassCopy = needScanBeansClass;
            needScanBeansClass = new HashSet<>();
            for (BeanDefinition scanBeanDefinition : scanBeanDefinitions) {
                String beanClassName = scanBeanDefinition.getBeanClassName();
                Class<?> clazz = ClassUtils.forName(beanClassName, getClass().getClassLoader());
                if (scannedCandidateComponent(needScanBeansClassCopy, clazz)) {
                    beanDefinitionCache.put(beanClassName, scanBeanDefinition);
                    searchNeedImportBeans(FieldClass.buildNoField(clazz));
                }
            }
        }
    }

    /**
     * 根据待测试类查找依赖树检索入口
     *
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private List<FieldClass> findEntryClass() throws InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {
        List<FieldClass> entryClasses = new LinkedList<>();
        List<Field> selfAndSuperFields = ClassFieldUtils.getSelfAndSuperFields(this.testClass);
        for (Field field : selfAndSuperFields) {
            if (judgeNeedScannedField(field)) {
                entryClasses.add(FieldClass.buildFieldClass(field));
            }
        }
        return entryClasses;
    }

    private void initAutoMockController() throws ClassNotFoundException {
        BeanDefinitionRegistry registry = BeanDefinitionRegistryUtil.getBeanDefinitionRegistry(context);
        ClassLoader classLoader = getClass().getClassLoader();
        List<String> classNames =
                SpringFactoriesLoader.loadFactoryNames(AutoMockTestController.class, classLoader);
        if (CollectionUtils.isEmpty(classNames)) {
            return;
        }
        log.info("Loaded default TestExecutionListener class names from location [{}]: {}",
                SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION, classNames);
        for (String className : classNames) {
            Class<?> clazz = ClassUtils.forName(className, classLoader);
            AutoMockTestController classInstance = (AutoMockTestController) BeanUtils.instantiateClass(clazz);
            classInstance.prepareController(testClass, registry);
            if (classInstance.enabled()) {
                autoMockTestControllers.add(classInstance);
            }
        }
    }

    /**
     * 初始化字段扫描判断器
     *
     * @throws ClassNotFoundException
     */
    private void initFieldScanJudges() throws ClassNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        List<String> classNames =
                SpringFactoriesLoader.loadFactoryNames(FieldScanJudge.class, classLoader);
        if (CollectionUtils.isEmpty(classNames)) {
            return;
        }
        log.info("Loaded default InjectedFieldJudge class names from location [{}]: {}",
                SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION, classNames);
        for (String className : classNames) {
            Class<?> clazz = ClassUtils.forName(className, classLoader);
            FieldScanJudge classInstance = (FieldScanJudge) BeanUtils.instantiateClass(clazz);
            fieldScanJudges.add(classInstance);
        }
    }

    private boolean filterAutoMock(FieldClass fieldClass) {
        if (mockBeans.contains(fieldClass.getClazz())) {
            return true;
        }
        for (AutoMockTestController autoMockTestController : autoMockTestControllers) {
            if (autoMockTestController.needAutoMock(fieldClass)) {
                return true;
            }
        }
        return false;
    }

    private boolean scannedCandidateComponent(Set<FieldClass> needScanBeansClass, Class<?> clazz) {
        for (FieldClass includeClass : needScanBeansClass) {
            if (includeClass.getClazz().isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检索需要引入当前上下文中的 spring bean
     *
     * @param fieldClass
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void searchNeedImportBeans(FieldClass fieldClass) throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Class<?> clazz = fieldClass.getClazz();
        if (scannedBeans.contains(fieldClass) || mockBeans.contains(clazz)) {
            return;
        }
        // 过滤需要自动 mock 的类，需要自动 mock 类直接跳过
        if (filterAutoMock(fieldClass)) {
            scannedBeans.add(fieldClass);
            return;
        }
        // 抽象类与接口，需要配合扫包，加入扫包类集合中
        int modifiers = clazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            needScanBeansClass.add(fieldClass);
        }
        Class<?> superClass = clazz.getSuperclass();
        while (Objects.nonNull(superClass)) {
            searchNeedImportBeans(FieldClass.buildNoField(superClass));
            superClass = superClass.getSuperclass();
        }
        scannedBeans.add(fieldClass);
        if (isCandidateBean(clazz)) {
            candidateBeans.add(fieldClass);
        }
        List<Field> fields = ClassFieldUtils.getFields(clazz);
        if (CollectionUtils.isEmpty(fields)) {
            return;
        }
        for (Field field : fields) {
            if (judgeNeedScannedField(field) && isNotMultipleBeans(field)) {
                searchNeedImportBeans(FieldClass.buildFieldClass(field));
            }
        }
    }

    /**
     * 判断是否为需要被扫描的字段
     *
     * @param field
     * @return
     */
    private boolean judgeNeedScannedField(Field field) {
        // 检验基础注解标注的待扫描字段
        Autowired autowired = field.getAnnotation(Autowired.class);
        Resource resource = field.getAnnotation(Resource.class);
        SpyBean spyBean = field.getAnnotation(SpyBean.class);
        if (Objects.nonNull(autowired) || Objects.nonNull(resource) || Objects.nonNull(spyBean)) {
            return Boolean.TRUE;
        }
        // 判断扩展注解标注的待扫描字段
        for (FieldScanJudge fieldScanJudge : fieldScanJudges) {
            if (fieldScanJudge.judgeScanField(field)) {
                return true;
            }
        }
        return Boolean.FALSE;
    }

    private AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
        if (ao.getAnnotations().length > 0) {
            for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }
        }
        return null;
    }

    /**
     * 判断字段是否非集合注入，注入字段类型为Map、List等
     *
     * @param field
     * @return
     */
    private boolean isNotMultipleBeans(Field field) {
        DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
        final Class<?> type = descriptor.getDependencyType();
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            ResolvableType resolvableType = descriptor.getResolvableType();
            Class<?> resolvedArrayType = resolvableType.resolve(type);
            if (resolvedArrayType != type) {
                componentType = resolvableType.getComponentType().resolve();
            }
            if (componentType == null) {
                return false;
            }
            needScanBeansClass.add(FieldClass.buildNoField(componentType));
            return false;
        } else if (Collection.class.isAssignableFrom(type) && type.isInterface()) {
            Class<?> elementType = descriptor.getResolvableType().asCollection().resolveGeneric();
            if (elementType == null) {
                return false;
            }
            needScanBeansClass.add(FieldClass.buildNoField(elementType));
            return false;
        } else if (Map.class == type) {
            ResolvableType mapType = descriptor.getResolvableType().asMap();
            Class<?> keyType = mapType.resolveGeneric(0);
            if (String.class != keyType) {
                return false;
            }
            Class<?> valueType = mapType.resolveGeneric(1);
            if (valueType == null) {
                return false;
            }
            needScanBeansClass.add(FieldClass.buildNoField(valueType));
            return false;
        }
        return true;
    }

    /**
     * 判断是否为候选 bean，被扫描到的类都在依赖树上，都是待生成的 spring bean
     * ① 被继承自 @Component 注解的注解标注的类，且不是抽象类或接口
     * ② @AutoImport 注解 includeSimpleClass = true，且不是抽象类或接口的类
     *
     * @param clazz
     * @return
     */
    private boolean isCandidateBean(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        Component componentAnno = AnnotationUtils.findAnnotation(clazz, Component.class);
        if (Objects.nonNull(componentAnno) && !Modifier.isAbstract(modifiers)
                && !Modifier.isInterface(modifiers)) {
            return true;
        }
        SpyBean spyBeanAnno = AnnotationUtils.findAnnotation(clazz, SpyBean.class);
        if (Objects.nonNull(spyBeanAnno) && !Modifier.isAbstract(modifiers)
                && !Modifier.isInterface(modifiers)) {
            return true;
        }
        if (this.annotation.includeSimpleClass()) {
            return !Modifier.isAbstract(modifiers) && !Modifier.isInterface(modifiers);
        }
        return false;
    }

    /**
     * 扫描所有需要被 mock 的 spring bean
     *
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     */
    private void scanMockBeans() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Field> selfAndSuperFields = ClassFieldUtils.getSelfAndSuperFields(this.testClass);
        for (Field field : selfAndSuperFields) {
            MockBean mockBean = field.getAnnotation(MockBean.class);
            if (Objects.nonNull(mockBean)) {
                mockBeans.add(field.getType());
            }
        }
    }

}
