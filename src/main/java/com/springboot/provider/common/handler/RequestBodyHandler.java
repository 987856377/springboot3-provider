package com.springboot.provider.common.handler;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.common.handler
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-14 14:19
 **/
@RestControllerAdvice
public class RequestBodyHandler implements RequestBodyAdvice {

    private final SymmetricCrypto symmetricCrypto;

    public RequestBodyHandler(SymmetricCrypto symmetricCrypto) {
        this.symmetricCrypto = symmetricCrypto;
    }

    /**
     * Invoked first to determine if this interceptor applies.
     * 此处如果返回false , 则不执行当前Advice的业务
     * @param methodParameter the method parameter
     * @param targetType      the target type, not necessarily the same as the method
     *                        parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType   the selected converter type
     * @return whether this interceptor should be invoked or not
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Invoked second before the request body is read and converted.
     * 此做些编码 / 解密 / 封装参数为对象的操作
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the input request or a new instance, never {@code null}
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return new MappingJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders());
//        return new HttpInputMessage() {
//            @Override
//            public InputStream getBody() throws IOException {
//                String data = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
//                byte[] decode = symmetricCrypto.decrypt(data);
//                return new ByteArrayInputStream(decode);
//            }
//
//            @Override
//            public HttpHeaders getHeaders() {
//                return inputMessage.getHeaders();
//            }
//        };
    }

    /**
     * Invoked third (and last) after the request body is converted to an Object.
     *
     * @param body          set to the converter Object before the first advice is called
     * @param inputMessage  the request
     * @param parameter     the target method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return the same body or a new instance
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * Invoked second (and last) if the body is empty.
     *
     * @param body          usually set to {@code null} before the first advice is called
     * @param inputMessage  the request
     * @param parameter     the method parameter
     * @param targetType    the target type, not necessarily the same as the method
     *                      parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the selected converter type
     * @return the value to use or {@code null} which may then raise an
     * {@code HttpMessageNotReadableException} if the argument is required.
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
