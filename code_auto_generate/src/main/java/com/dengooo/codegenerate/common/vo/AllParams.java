package com.dengooo.codegenerate.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class AllParams {
    private List<Params> params;
    private String packageName;
    private String entityName;
    private String tablePrefix;
}
