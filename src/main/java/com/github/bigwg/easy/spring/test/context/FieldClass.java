package com.github.bigwg.easy.spring.test.context;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 字段与类
 *
 * @author zhaozhiwei
 * @since 2024/3/21
 */
@Getter
@Setter
@ToString
public class FieldClass {

    private Field field;

    private Class<?> clazz;

    public static FieldClass buildFieldClass(Field field) {
        FieldClass fieldClass = new FieldClass();
        fieldClass.setField(field);
        fieldClass.setClazz(field.getType());
        return fieldClass;
    }

    public static FieldClass buildFieldClass(Field field, Class<?> clazz) {
        FieldClass fieldClass = new FieldClass();
        fieldClass.setField(field);
        fieldClass.setClazz(clazz);
        return fieldClass;
    }

    public static FieldClass buildNoField(Class<?> clazz) {
        FieldClass fieldClass = new FieldClass();
        fieldClass.setClazz(clazz);
        return fieldClass;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FieldClass)) {
            return false;
        }
        FieldClass other = (FieldClass) obj;
        return Objects.equals(this.field, other.getField())
                && Objects.equals(this.clazz, other.getClazz());
    }

    @Override
    public int hashCode() {
        int result = Objects.isNull(this.field) ? "null".hashCode() : this.field.hashCode();
        result += this.clazz.hashCode();
        return result;
    }
}
