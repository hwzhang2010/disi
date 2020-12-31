package com.hywx.common.generator.code;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.hywx.common.core.util.TimeUtil;

import java.util.*;

/**
 * @program: common
 * @description: 自动生成代码模板
 * @author: tangjing
 * @create: 2020-01-15 14:25
 **/
public class GeneratorTemplateUtil {


    public static void main(String[] args){

        //用来获取Mybatis-Plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("generator/generatorConfig");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(rb.getString("OutputDir"));
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setAuthor(rb.getString("author"));
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
//        gc.setIdType(IdType.UUID);
//        gc.setDateType(DateType.TIME_PACK);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl(rb.getString("url"));
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(rb.getString("username"));
        dsc.setPassword(rb.getString("password"));
        mpg.setDataSource(dsc);


        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(rb.getString("parent"));
        pc.setController("controller" + rb.getString("className"));
        pc.setService("service" + rb.getString("className"));
        pc.setServiceImpl("service" + rb.getString("className") + ".impl");
        pc.setEntity("dao" + rb.getString("className"));
        pc.setMapper("mapper" + rb.getString("className"));
        mpg.setPackageInfo(pc);

        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.ftl(或者是.vm)模板中，通过${cfg.dateTime}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("dateTime", TimeUtil.dateToString(new Date(),TimeUtil.FORMAT_STR_LONG));
                this.setMap(map);
            }
        };
        mpg.setCfg(injectionConfig);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        // 如果模板引擎是 freemarker
        // String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        String templatePath = "/generator/mapper.xml.vm";
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return rb.getString("OutputDirXml") + tableInfo.getEntityName() +"Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        TemplateConfig templateConfig = new TemplateConfig()
                .setEntity("generator/entity.java")
                .setController("generator/controller.java")
                .setService("generator/service.java")
                .setServiceImpl("generator/serviceImpl.java")
                .setMapper("generator/mapper.java")
                .setXml(null);

        mpg.setTemplate(templateConfig);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);// true不生产getSet方法
        strategy.setInclude(new String[]{rb.getString("tableName")});
        strategy.setTablePrefix(rb.getString("tablePrefix"));
        mpg.setStrategy(strategy);
        // mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // mpg.setTemplateEngine(new CustomTemplateEngine());
        mpg.execute();
    }
}