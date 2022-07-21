package com.springboot.provider.common.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceUtils {

    /**
     *  获取 resources 目录下的资源文件
     *  ep: db/quartz_mysql.sql
     * @param fileName
     * @return
     */
    public static String getResource(String fileName) {
        Assert.notNull(fileName, "fileName must not be null");

        String content = "";
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     *  获取 resources 目录下的资源文件
     *  ep: db/quartz_mysql.sql
     * @param fileName
     * @return
     */
    public static String getResource(ResourceLoader resourceLoader, String fileName) {
        Assert.notNull(fileName, "fileName must not be null");

        String content = "";
        Resource resource = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResource(fileName);
        try {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
