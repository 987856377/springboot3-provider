package com.springboot.provider.common.interceptor;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.map.MapUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.gson.Gson;
import com.springboot.provider.common.filter.RepeatedlyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.util.Map;

/**
 * web的调用时间统计拦截器
 * dev环境有效
 *
 * @author Lion Li
 * @since 3.3.0
 */
public class HttpRequestHandlerInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Gson gson = new Gson();

    private final TransmittableThreadLocal<StopWatch> invokeTimeTL = new TransmittableThreadLocal<>();
    private final TransmittableThreadLocal<String> invokeIdTL = new TransmittableThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getMethod() + " " + request.getRequestURI();

        String invokeId = NanoId.randomNanoId();

        // 打印请求参数
        if (isJsonRequest(request)) {
            String jsonParam = "";
            if (request instanceof RepeatedlyRequestWrapper) {
                BufferedReader reader = request.getReader();
                jsonParam = IoUtil.read(reader);
            }
            logger.info("InvokeId: {}, Request URL: {}, Json Parameter: {}", invokeId, url, jsonParam);
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (MapUtil.isNotEmpty(parameterMap)) {
                String parameters = gson.toJson(parameterMap);
                logger.info("InvokeId: {}, Request URL: {}, Param Parameter: {}", invokeId, url, parameters);
            } else {
                logger.info("InvokeId: {}, Request URL: {}, No Parameter", invokeId, url);
            }
        }

        StopWatch stopWatch = new StopWatch();
        invokeTimeTL.set(stopWatch);
        invokeIdTL.set(invokeId);
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        StopWatch stopWatch = invokeTimeTL.get();
        stopWatch.stop();
        logger.info("InvokeId: {}, Request URL: {}, Cost:{}ms", invokeIdTL.get(), request.getMethod() + " " + request.getRequestURI(), stopWatch.getTime());
        invokeTimeTL.remove();
        invokeIdTL.remove();
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE);
        }
        return false;
    }

}
