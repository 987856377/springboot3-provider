package com.springboot.provider.common.lifecycle;

import com.springboot.provider.common.exception.CacheCompletelyBrokenException;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.verifier
 * @Author xuzhenkui
 * @Date 2021-05-2516:35
 */
@Component
public class LocalCacheVerifier {
    private final ApplicationEventPublisher eventPublisher;

    public LocalCacheVerifier(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void checkLocalCache() {
        try {
            // ...
            doCheck();
        } catch (CacheCompletelyBrokenException ex) {
            AvailabilityChangeEvent.publish(this.eventPublisher, ex, LivenessState.BROKEN);
        }
    }

    public void doCheck() throws CacheCompletelyBrokenException {
        System.out.println("[LocalCacheVerifier] doCheck");
        throw new CacheCompletelyBrokenException("[LocalCacheVerifier] check error");
    }

}
