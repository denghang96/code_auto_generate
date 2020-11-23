package com.dengooo.codegenerate.service.impl;

import com.dengooo.codegenerate.common.vo.AllParams;
import com.dengooo.codegenerate.common.vo.Params;
import com.dengooo.codegenerate.config.CustomProperties;
import com.dengooo.codegenerate.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Resource
    private CustomProperties customProperties;

    @Override
    public void generate(AllParams allParams) throws IOException {
        String mainPackage = customProperties.getFilePath()+allParams.getPackageName().replaceAll("\\.","/")+"/";
        log.info(mainPackage);
        //先生成所有的包
        //1.主包 com.xxx.xxx
        File mainPackageFile = new File(mainPackage);
        if (!mainPackageFile.exists()) {
            mainPackageFile.mkdirs();
        }
        log.info("mainPackageFileL {}", mainPackageFile.getAbsolutePath());

        //2.controller
        String controllerPackage = mainPackage + "controller/";
        File controllerPackageFile = new File(controllerPackage);
        if (!controllerPackageFile.exists()) {
            controllerPackageFile.mkdirs();
        }
        log.info("controllerPackageFile {}", controllerPackageFile.getAbsolutePath());


        //3.dao
        String daoMapperPackage = mainPackage + "dao/";
        String daoXmlPackage = mainPackage + "dao/xml/";
        File daoPackageFile = new File(daoXmlPackage);
        if (!daoPackageFile.exists()) {
            daoPackageFile.mkdirs();
        }
        log.info("daoPackageFile {}",daoPackageFile.getAbsolutePath());
        //4.entity
        String entityPackage = mainPackage + "entity/";
        File entityPackageFile = new File(entityPackage);
        if (!entityPackageFile.exists()) {
            entityPackageFile.mkdirs();
        }
        log.info("entityPackageFile {}",entityPackageFile.getAbsolutePath());
        //5.service
        String serviceInterfacePackage = mainPackage + "service/";
        String servicePackage = mainPackage + "service/impl/";
        File servicePackageFile = new File(servicePackage);
        if (!servicePackageFile.exists()) {
            servicePackageFile.mkdirs();
        }
        log.info(servicePackageFile.getAbsolutePath());
        //6.vo.req
        String reqVoPackage = mainPackage + "vo/req/";
        File reqVoPackageFile = new File(reqVoPackage);
        if (!reqVoPackageFile.exists()) {
            reqVoPackageFile.mkdirs();
        }
        log.info("reqVoPackageFile {}",reqVoPackageFile.getAbsolutePath());
        //7.vo.resp
        String respVoPackage = mainPackage + "vo/resp/";
        File respVoPackageFile = new File(respVoPackage);
        if (!respVoPackageFile.exists()) {
            respVoPackageFile.mkdirs();
        }
        log.info("respVoPackageFile {}",respVoPackageFile.getAbsolutePath());
        //8.生成entity
        generateEntity(entityPackage, allParams);
        //9.生成controller
        generateController(controllerPackage, allParams);
        //10.生成Service接口
        generateServiceInterface(serviceInterfacePackage, allParams);
        //11.生成reqVo
        generateReqVo(reqVoPackage, allParams);
        //12.生成响应Vo
        generateRespVo(respVoPackage, allParams);
        //13.生成Service实现类
        generateService(servicePackage, allParams);
        //14.生成Mapper接口
        generateMapperInterface(daoMapperPackage, allParams);
        //14.生成Xml
        generateMapperXml(daoXmlPackage, allParams);
    }
    /*
        生成entity
     */
    private void generateEntity(String entityPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String fileName = entityName + ".java";
        File file = new File(entityPackage, fileName);
        log.info("file {}",file.getAbsolutePath());
        List<Params> properties = allParams.getParams();
        Set<String> propertyTypes = properties.stream().map(Params::getPropertyType).collect(Collectors.toSet());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".entity;\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.extension.activerecord.Model;\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.annotation.TableName;\r\n");
        stringBuffer.append("import java.io.Serializable;\r\n");
        stringBuffer.append("import lombok.Data;\r\n");
        propertyTypes.forEach(propertyType -> {
            if (Objects.equals(propertyType,"Date")) {
                stringBuffer.append("import java.util.Date;\r\n");
            }
        });
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("@Data\r\n");
        stringBuffer.append("@TableName(\""+humpToUnderline(allParams.getEntityName())+"\")\r\n");
        stringBuffer.append("public class "+entityName+" extends BaseEntity<"+entityName+"> implements Serializable { \r\n");
        stringBuffer.append("    private static final long serialVersionUID = 1L; \r\n");

        properties.forEach(propertie -> {
            String pn = propertie.getPropertyName();
            String pt = propertie.getPropertyType();
            if (!StringUtils.isEmpty(pn)) {
                stringBuffer.append("    private "+pt+" "+pn+";\r\n");
            }
        } );

        stringBuffer.append("    @Override\r\n");
        stringBuffer.append("    protected Serializable pkVal() {\n");
        stringBuffer.append("        return this.id;\n");
        stringBuffer.append("    }\r\n");
        stringBuffer.append("}");

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /*
        生成controller
     */
    public void generateController(String controllerPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String controllerName = entityName+"Controller";
        String fileName = controllerName + ".java";
        File file = new File(controllerPackage, fileName);

        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package ").append(allParams.getPackageName()).append(".controller;\r\n\r\n");
        stringBuffer.append("import ").append(customProperties.getRespDataPath()).append(".").append(customProperties.getRespDataName()).append(";\r\n");
        stringBuffer.append("import ").append(allParams.getPackageName()).append(".service.I").append(entityName).append("Service;\r\n\r\n");

        stringBuffer.append("import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.req."+toUpperFirstCode(entityName)+"ConditionsReqVo;\n\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".entity."+entityName+";\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.resp."+entityName+"ConditionsRespVo;\r\n");
        stringBuffer.append("import org.springframework.web.bind.annotation.*;\r\n");
        stringBuffer.append("import javax.annotation.Resource;\r\n\n");
        stringBuffer.append("import java.util.Date;\r\n");
        stringBuffer.append("import java.util.List;\r\n\n");
        stringBuffer.append("@RestController\r\n");
        stringBuffer.append("@RequestMapping(\"").append(customProperties.getPrefixRequestMapping()).append(toLowFirstCode(entityName)).append("/\")\r\n");
        stringBuffer.append("public class ").append(entityName).append("Controller {\r\n\n");
        stringBuffer.append("    @Resource\r\n");
        stringBuffer.append("    I").append(entityName).append("Service ").append(toLowFirstCode(entityName)).append("Service;\r\n\n");
        List<Params> selectParams = allParams.getParams().stream().filter(Params::getIsSingleCondition).collect(Collectors.toList());
        //根据查询条件生成Controller方法
        selectParams.forEach(param -> {
            if (!param.getIsSingleConditionList()) {
                stringBuffer.append("    @GetMapping(\"/getBy").append(toUpperFirstCode(param.getPropertyName())).append("\")\r\n");
                stringBuffer.append("    public RespData getBy").append(toUpperFirstCode(param.getPropertyName())).append("(").append(param.getPropertyType()).append(" ").append(param.getPropertyName()).append(") {\r\n");
                //如果属性是id
                if ("id".equals(param.getPropertyName())) {
                    stringBuffer.append("        return RespData.success(").append(toLowFirstCode(entityName)).append("Service.getById(id));\r\n");
                } else {
                    stringBuffer.append("        QueryWrapper<"+entityName+"> "+toLowFirstCode(entityName)+"QueryWrapper = new QueryWrapper<>();\r\n");
                    stringBuffer.append("        "+toLowFirstCode(entityName)+"QueryWrapper.eq(\""+humpToUnderline(param.getPropertyName())+"\", "+param.getPropertyName()+");\r\n");
                    stringBuffer.append("        return RespData.success("+toLowFirstCode(entityName)+"Service.getOne(sysUserQueryWrapper));\r\n");
                }
            }else {
                //根据查询条件生成Controller方法
                //如果属性是id
                if ("id".equals(param.getPropertyName())) {
                    stringBuffer.append("    @GetMapping(\"/getBy").append(toUpperFirstCode(param.getPropertyName())).append("\")\r\n");
                    stringBuffer.append("    public RespData getBy").append(toUpperFirstCode(param.getPropertyName())).append("(").append(param.getPropertyType()).append(" ").append(param.getPropertyName()).append(") {\r\n");
                    stringBuffer.append("        return RespData.success(").append(toLowFirstCode(entityName)).append("Service.getById(id));\r\n");
                } else {
                    stringBuffer.append("    @GetMapping(\"/selectBy").append(toUpperFirstCode(param.getPropertyName())).append("\")\r\n");
                    stringBuffer.append("    public RespData selectBy").append(toUpperFirstCode(param.getPropertyName())).append("(Page page, ").append(param.getPropertyType()).append(" ").append(param.getPropertyName()).append(") {\r\n");
                    stringBuffer.append("        QueryWrapper<"+entityName+"> "+toLowFirstCode(entityName)+"QueryWrapper = new QueryWrapper<>();\r\n");
                    stringBuffer.append("        sysUserQueryWrapper.eq(\""+humpToUnderline(param.getPropertyName())+"\", "+param.getPropertyName()+");\r\n");
                    stringBuffer.append("        return RespData.success("+toLowFirstCode(entityName)+"Service.page(page, sysUserQueryWrapper));\r\n");
                }
            }

            stringBuffer.append("    }\r\n");
            stringBuffer.append("\r\n");
        });

        //根据复合查询条件生成方法
        List<Params> conditionsParam = allParams.getParams().stream().filter(Params::getIsCondition).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(conditionsParam)) {
            stringBuffer.append("    @GetMapping(\"/selectByConditions\")\r\n");
            stringBuffer.append("    public RespData selectByConditions(Page page, "+entityName+"ConditionsReqVo "+toLowFirstCode(entityName)+"ConditionsReqVo) {\r\n");
            stringBuffer.append("        List<"+entityName+"ConditionsRespVo> "+toLowFirstCode(entityName)+"ConditionsRespVos = "+toLowFirstCode(entityName)+"Service.selectByConditions(page, "+toLowFirstCode(entityName)+"ConditionsReqVo);\r\n");
            stringBuffer.append("        page.setRecords("+toLowFirstCode(entityName)+"ConditionsRespVos);\r\n");
            stringBuffer.append("        return RespData.success(page);\r\n");
            stringBuffer.append("    }\r\n");
        }

        stringBuffer.append("}");

        bufferedWriter.write(stringBuffer.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }
    /*
        生成Service接口
     */
    public void generateServiceInterface(String servicePackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String interfaceName = "I"+entityName+"Service";
        String fileName = interfaceName + ".java";
        File file = new File(servicePackage, fileName);

        FileWriter fileWriter = new FileWriter(file);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".service;\r\n\r\n");

        stringBuffer.append("import com.baomidou.mybatisplus.extension.service.IService;\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.req."+entityName+"ConditionsReqVo;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.resp."+entityName+"ConditionsRespVo;\r\n");
        stringBuffer.append("import java.util.List;\r\n");

        stringBuffer.append("import "+allParams.getPackageName()+".entity."+toUpperFirstCode(entityName)+";\r\n\r\n");
        stringBuffer.append("public interface "+interfaceName+" extends IService<"+entityName+"> {\r\n\n");
        stringBuffer.append("    List<"+entityName+"ConditionsRespVo> selectByConditions(Page page, "+entityName+"ConditionsReqVo "+toLowFirstCode(entityName)+"ConditionsReqVo);\r\n\n");
        stringBuffer.append("}");

        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /*
        生成请求Vo
     */
    public void generateReqVo(String reqVoPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String fileName = entityName + "ConditionsReqVo.java";
        File file = new File(reqVoPackage, fileName);
        List<Params> properties = allParams.getParams();
        Set<String> propertyTypes = properties.stream().filter(Params::getIsCondition).map(Params::getPropertyType).collect(Collectors.toSet());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".vo.req;\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("import java.io.Serializable;\r\n");
        stringBuffer.append("import lombok.Data;\r\n");
        propertyTypes.forEach(propertyType -> {
            if (Objects.equals(propertyType,"Date")) {
                stringBuffer.append("import java.util.Date;\r\n");
            }
        });
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("@Data\r\n");
        stringBuffer.append("public class "+entityName+"ConditionsReqVo implements Serializable { \r\n");
        stringBuffer.append("   private static final long serialVersionUID = 1L; \r\n");

        properties.stream().filter(Params::getIsCondition).collect(Collectors.toList()).forEach(propertie -> {
            String pn = propertie.getPropertyName();
            String pt = propertie.getPropertyType();
            if (!StringUtils.isEmpty(pn)) {
                stringBuffer.append("   private "+pt+" "+pn+";\r\n");
            }
        });

        stringBuffer.append("}");

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }
    /*
        生成响应Vo
     */
    public void generateRespVo(String respVoPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String fileName = entityName + "ConditionsRespVo.java";
        File file = new File(respVoPackage, fileName);
        List<Params> properties = allParams.getParams();
        Set<String> propertyTypes = properties.stream().filter(Params::getIsReturn).map(Params::getPropertyType).collect(Collectors.toSet());

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".vo.resp;\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("import java.io.Serializable;\r\n");
        stringBuffer.append("import lombok.Data;\r\n");
        propertyTypes.forEach(propertyType -> {
            if (Objects.equals(propertyType,"Date")) {
                stringBuffer.append("import java.util.Date;\r\n");
            }
        });
        stringBuffer.append("\r\n");
        stringBuffer.append("\r\n");
        stringBuffer.append("@Data\r\n");
        stringBuffer.append("public class "+entityName+"ConditionsRespVo implements Serializable { \r\n");
        stringBuffer.append("   private static final long serialVersionUID = 1L; \r\n");

        properties.stream().filter(Params::getIsReturn).collect(Collectors.toList()).forEach(propertie -> {
            String pn = propertie.getPropertyName();
            String pt = propertie.getPropertyType();
            if (!StringUtils.isEmpty(pn)) {
                stringBuffer.append("   private "+pt+" "+pn+";\r\n");
            }
        } );

        stringBuffer.append("}");

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }
    /*
        生成Service
     */
    public void generateService(String servicePackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String serviceName = entityName+"Service";
        String fileName = serviceName + ".java";
        File file = new File(servicePackage, fileName);

        FileWriter fileWriter = new FileWriter(file);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".service.impl;\r\n\r\n");

        stringBuffer.append("import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".dao."+entityName+"Mapper;\r\n");
        stringBuffer.append("import com.graduation.project.service.I"+entityName+"Service;\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.req."+entityName+"ConditionsReqVo;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.resp."+entityName+"ConditionsRespVo;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".entity."+entityName+";\r\n");
        stringBuffer.append("import java.util.List;\r\n\n");


        stringBuffer.append("import "+allParams.getPackageName()+".entity."+toUpperFirstCode(entityName)+";\r\n");
        stringBuffer.append("import org.springframework.stereotype.Service;\r\n");
        stringBuffer.append("import javax.annotation.Resource;\r\n\n");

        stringBuffer.append("@Service\r\n");
        stringBuffer.append("public class "+serviceName+" extends ServiceImpl<"+entityName+"Mapper, "+entityName+"> implements I"+entityName+"Service {\r\n\n");
        stringBuffer.append("    @Resource\r\n");
        stringBuffer.append("    "+entityName+"Mapper "+toLowFirstCode(entityName)+"Mapper;\r\n\n");
        stringBuffer.append("    @Override\r\n");
        stringBuffer.append("    public List<"+entityName+"ConditionsRespVo> selectByConditions(Page page, "+entityName+"ConditionsReqVo "+toLowFirstCode(entityName)+"ConditionsReqVo) {\r\n");
        stringBuffer.append("        List<"+entityName+"ConditionsRespVo> "+toLowFirstCode(entityName)+"ConditionsRespVos = "+toLowFirstCode(entityName)+"Mapper.selectByConditions(page, "+toLowFirstCode(entityName)+"ConditionsReqVo);\n");
        stringBuffer.append("        return "+toLowFirstCode(entityName)+"ConditionsRespVos;\n");
        stringBuffer.append("    }\r\n");
        stringBuffer.append("}");

        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /*
        生成MapperInterface
     */
    public void generateMapperInterface(String daoMapperPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String mapperName = entityName+"Mapper";
        String fileName = mapperName + ".java";
        File file = new File(daoMapperPackage, fileName);

        FileWriter fileWriter = new FileWriter(file);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("package "+allParams.getPackageName()+".dao;\r\n\r\n");

        stringBuffer.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\r\n");
        stringBuffer.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.req."+entityName+"ConditionsReqVo;\r\n");
        stringBuffer.append("import "+allParams.getPackageName()+".vo.resp."+entityName+"ConditionsRespVo;\r\n");
        stringBuffer.append("import java.util.List;\r\n");

        stringBuffer.append("import "+allParams.getPackageName()+".entity."+toUpperFirstCode(entityName)+";\r\n");
        stringBuffer.append("import org.apache.ibatis.annotations.Param;\r\n");
        stringBuffer.append("import org.springframework.stereotype.Repository;\r\n\r\n");

        stringBuffer.append("@Repository\r\n");
        stringBuffer.append("public interface "+mapperName+" extends BaseMapper<"+entityName+"> {\r\n\n");
        stringBuffer.append("    List<"+entityName+"ConditionsRespVo> selectByConditions(Page page, @Param(\""+entityName+"ConditionsReqVo\")"+entityName+"ConditionsReqVo "+toLowFirstCode(entityName)+"ConditionsReqVo);\r\n\n");
        stringBuffer.append("}");

        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /*
        生成MapperXml
     */
    public void generateMapperXml(String daoXmlPackage, AllParams allParams) throws IOException {
        String entityName = allParams.getEntityName();
        String mapperName = entityName+"Mapper";
        String fileName = mapperName + ".xml";
        File file = new File(daoXmlPackage, fileName);

        FileWriter fileWriter = new FileWriter(file);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        stringBuffer.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n\n");
        stringBuffer.append("<mapper namespace=\""+allParams.getPackageName()+".dao."+allParams.getEntityName()+"Mapper\">");
        stringBuffer.append("    <!-- 通用查询映射结果 -->\r\n");
        stringBuffer.append("    <resultMap id=\"BaseResultMap\" type=\""+allParams.getPackageName()+".entity."+entityName+"\">\r\n");
        allParams.getParams().forEach(param -> {
            if ("id".equals(param.getPropertyName())) {
                stringBuffer.append("        <id column=\"id\" property=\"id\" />\n");
            }else {
                stringBuffer.append("        <result column=\""+humpToUnderline(param.getPropertyName())+"\" property=\""+param.getPropertyName()+"\" />\n");
            }
        });
        stringBuffer.append("    </resultMap> \r\n\n");

        stringBuffer.append("    <!-- 通用查询结果列 --> \r\n");
        stringBuffer.append("    <sql id=\"Base_Column_List\"> \r\n");
        List<String> propertiesName = allParams.getParams().stream().map(Params::getPropertyName).collect(Collectors.toList());
        stringBuffer.append("       "+String.join(",", propertiesName)+"\r\n");
        stringBuffer.append("    </sql>\r\n\n");

        stringBuffer.append("    <select id=\"selectByConditions\" resultType=\""+allParams.getPackageName()+".vo.resp."+entityName+"ConditionsRespVo\"> \r\n");
        stringBuffer.append("        SELECT\r\n");
        List<String> resultNames = allParams.getParams().stream().filter(Params::getIsReturn).map(Params::getPropertyName).collect(Collectors.toList());

        stringBuffer.append("            "+String.join(",", resultNames)+"\r\n");
        stringBuffer.append("        FROM\r\n");
        stringBuffer.append("            "+allParams.getTablePrefix()+humpToUnderline(entityName)+"\r\n");
        stringBuffer.append("        WHERE 1 = 1\r\n");
        List<Params> queryParams = allParams.getParams().stream().filter(Params::getIsCondition).collect(Collectors.toList());
        queryParams.forEach(queryParam -> {
            if ("String".equals(queryParam.getPropertyType())) {
                stringBuffer.append("        <if test=\""+toLowFirstCode(entityName)+"ConditionsReqVo."+queryParam.getPropertyName()+" != null and "+toLowFirstCode(entityName)+"ConditionsReqVo."+queryParam.getPropertyName()+" != ''\">\r\n");
                stringBuffer.append("            AND "+humpToUnderline(queryParam.getPropertyName())+" = #{"+toLowFirstCode(entityName)+"ConditionsReqVo."+queryParam.getPropertyName()+"}\r\n");
                stringBuffer.append("        </if>\r\n");
            }
        });
        stringBuffer.append("    </select>\r\n");
        stringBuffer.append("</mapper>");

        fileWriter.write(stringBuffer.toString());
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String humpToUnderline(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成小写
            result.append(name.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * 首字母转小写
     * @param name
     * @return
     */
    public static String toLowFirstCode(String name) {
        String s = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        return s;
    }

    /**
     * 首字母转打写
     * @param name
     * @return
     */
    public static String toUpperFirstCode(String name) {
        String s = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        return s;
    }
}
