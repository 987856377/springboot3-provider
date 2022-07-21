package com.springboot.provider.common.handler;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.common.handler
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-14 14:14
 **/
@RestControllerAdvice
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    private final SymmetricCrypto symmetricCrypto;

    public ResponseBodyHandler(ObjectMapper objectMapper, SymmetricCrypto symmetricCrypto) {
        this.objectMapper = objectMapper;
        this.symmetricCrypto = symmetricCrypto;
    }

    /**
     * Whether this component supports the given controller method return type
     * and the selected {@code HttpMessageConverter} type.
     *
     * @param returnType    the return type
     * @param converterType the selected converter type
     * @return {@code true} if {@link #beforeBodyWrite} should be invoked;
     * {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Invoked after an {@code HttpMessageConverter} is selected and just before
     * its write method is invoked.
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     * @return the body that was passed in or a modified (possibly new) instance
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

//        if (body instanceof ResultJson) {
//            try {
//                byte[] bytes = objectMapper.writeValueAsBytes(body);
//                return symmetricCrypto.encrypt(bytes);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }
        return body;
    }
}
