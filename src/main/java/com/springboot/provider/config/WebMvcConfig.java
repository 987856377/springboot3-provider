package com.springboot.provider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Value("${spring.servlet.header.location}")
    private String header;

    @Value("${spring.servlet.multipart.location}")
    private String file;

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Return a handler mapping ordered at 1 to map URL paths directly to
     * view names. To configure view controllers, override
     * {@link #addViewControllers}.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/upload").setViewName("uploadForm");
    }

    /**
     * Override this method to configure cross origin requests processing.
     *
     * @see CorsRegistry
     * @since 4.2
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .exposedHeaders("Authorization")
                .maxAge(3600);
    }

    /**
     * Override this method to add resource handlers for serving static resources.
     *
     * @see ResourceHandlerRegistry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://127.0.0.1:8090/img/header.jpg
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + header);
        // http://127.0.0.1:8090/file/ssr.txt
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + file);

        registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    }

    /**
     * Override this method to configure content negotiation.
     *
     * @param configurer
     * @see DefaultServletHandlerConfigurer
     */
    @Override
    protected void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML, MediaType.TEXT_PLAIN);
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(objectMapper);
            }
        });
    }
}