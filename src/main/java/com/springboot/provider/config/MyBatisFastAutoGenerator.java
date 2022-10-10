package com.springboot.provider.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.springboot.provider.common.utils.PropertyUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Project mybatisplus
 * @Package com.spring.mybatisplus.configuration
 * @Author xuzhenkui
 * @Date 2019/10/5 20:56
 */
public class MyBatisFastAutoGenerator {

    private static final String AUTHOR = "XuZhenkui";

    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String OUTPUT_DIR = PROJECT_PATH + "/src/main/java/";
    private static final String MAPPER_PATH = PROJECT_PATH + "/src/main/resources/mapper/";

    private static final String BASE_PACKAGE = "com.springboot.provider.module";
    private static String ORGANIZATION;

    static {
        try {
            Object profile = Objects.requireNonNull(PropertyUtils.load("application.properties", "spring.profiles.active")).get(0);
            ORGANIZATION = String.valueOf(profile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create("jdbc:mysql://localhost:3306/test?useAffectedRows=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false",
                "root", "root");

        String moduleName = fastAutoGenerator.scannerNext("请输入模块名: ");
        String domain = fastAutoGenerator.scannerNext("请输入所属域(eg: HIS, LIS... ): ").toLowerCase();
        String tables = fastAutoGenerator.scannerNext("表名，多个英文逗号分割? 所有输入 all: ");

        fastAutoGenerator
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .dateType(DateType.ONLY_DATE)
                            .outputDir(OUTPUT_DIR) // 指定输出目录
                            .disableOpenDir();
                })
                .packageConfig(builder -> {
                    builder.parent(BASE_PACKAGE) // 设置父包名
                            .moduleName(moduleName) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, MAPPER_PATH + ORGANIZATION + "/" + domain + "/" + moduleName + "/")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.enableCapitalMode()
                            .addInclude(getTables(tables))
                            .entityBuilder()
                            .enableActiveRecord()
                            .superClass(com.springboot.provider.common.BaseEntity.class)
                            .disableSerialVersionUID()
                            .enableChainModel()
                            .idType(IdType.ASSIGN_ID)
                            .enableTableFieldAnnotation()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .addTableFills(Arrays.asList(
                                    new Column("create_time", FieldFill.INSERT),
                                    new Column("update_time", FieldFill.UPDATE),
                                    new Column("delete_flag", FieldFill.INSERT),
                                    new Column("version", FieldFill.INSERT)
                            ))
                            .logicDeleteColumnName("delete_flag")
                            .versionColumnName("version")
                            .build()
                            .mapperBuilder()
                            .enableBaseColumnList()
                            .enableBaseResultMap()
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableHyphenStyle()
                            .build();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
