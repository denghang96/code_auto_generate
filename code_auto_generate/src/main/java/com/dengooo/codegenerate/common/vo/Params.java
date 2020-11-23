package com.dengooo.codegenerate.common.vo;

import lombok.Data;

@Data
public class Params {
    private String propertyName;
    private String propertyType;
    private Boolean isSingleCondition;
    private Boolean isSingleConditionList;
    private Boolean isCondition;
    private Boolean isReturn;
}
