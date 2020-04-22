package com.c0ying.framework.exceldata.imparse;

import java.util.List;

public interface CustomTransformBean<T> {

    /**
     * 自定义转换逻辑
     * @param obj 实体
     * @param value 当前值
     * @param index 当前列索引
     * @param fieldsMapping 当前字段映射
     */
    void customTransform(T obj, String value, int index, List<String> fieldsMapping);

    /**
     * 自定义转换逻辑
     * @param obj 实体
     * @param value 当前值
     * @param index 当前列索引
     * @param fieldsMapping 当前字段映射
     * @param values 当前行数值
     */
    default void customTransform(T obj, String value, int index, List<String> fieldsMapping, List<String> values) {
        customTransform(obj, value, index, fieldsMapping);
    }
}
