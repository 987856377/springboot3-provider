package com.springboot.provider.common.holder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.common.holder
 * @description
 * @author: XuZhenkui
 * @create: 2021-01-29 09:22
 **/
public class HttpServletRequestContextHolder {

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // RequestContextHolder.currentRequestAttributes();

        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // RequestContextHolder.currentRequestAttributes();

        assert requestAttributes != null;
        return ((ServletRequestAttributes) requestAttributes).getResponse();
    }


    public static String getAttributeFromSession(String name) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // RequestContextHolder.currentRequestAttributes();

        assert requestAttributes != null;
        return (String) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    public static String getAttributeFromRequest(String name) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes(); // RequestContextHolder.currentRequestAttributes();

        assert requestAttributes != null;
        return (String) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}
