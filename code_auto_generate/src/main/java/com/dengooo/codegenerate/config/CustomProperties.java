package com.dengooo.codegenerate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration //所有的初始化配置都需要加这个注解
@ConfigurationProperties(prefix = "package")
public class CustomProperties {
    /*
        代码的生成路径
     */
    private String filePath;
    /*
        返回对象的名称
     */
    private String respDataName;
    /*
       返回对象的路径
     */
    private String respDataPath;
    /*
        controller的requestMapping前缀
     */
    private String prefixRequestMapping;
}
