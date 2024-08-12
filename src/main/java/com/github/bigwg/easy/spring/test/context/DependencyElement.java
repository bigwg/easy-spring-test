package com.github.bigwg.easy.spring.test.context;

import java.lang.reflect.Member;

/**
 * 依赖项，用来描述扫描到的Spring Bean中的依赖，包括字段、方法和构造器
 *
 * @author zhaozhiwei
 * @since 2024/6/9
 */
public class DependencyElement {

    private final Member member;

    private final Class<?> resourceType;

    private final boolean directDepend;

    public DependencyElement(Member member, Class<?> resourceType, boolean directDepend) {
        this.member = member;
        this.resourceType = resourceType;
        this.directDepend = directDepend;
    }

    public static DependencyElement buildWithMember(Member member, Class<?> resourceType) {
        return new DependencyElement(member, resourceType, false);
    }

    public static DependencyElement buildDirectDepend(Class<?> resourceType) {
        return new DependencyElement(null, resourceType, true);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DependencyElement)) {
            return false;
        }
        DependencyElement otherElement = (DependencyElement) other;
        return this.member.equals(otherElement.member);
    }

    @Override
    public int hashCode() {
        return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " for " + this.member;
    }

}
