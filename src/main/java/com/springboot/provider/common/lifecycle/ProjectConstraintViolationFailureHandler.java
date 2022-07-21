package com.springboot.provider.common.lifecycle;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021-05-27 09:45
 */
public class ProjectConstraintViolationFailureHandler extends AbstractFailureAnalyzer {
    /**
     * Returns an analysis of the given {@code rootFailure}, or {@code null} if no
     * analysis was possible.
     *
     * @param rootFailure the root failure passed to the analyzer
     * @param cause       the actual found cause
     * @return the analysis or {@code null}
     */
    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, Throwable cause) {
        System.out.println("[FailureAnalyzer] analyze");
        return new FailureAnalysis(rootFailure.getMessage(), cause.getMessage(), cause);
    }
}
