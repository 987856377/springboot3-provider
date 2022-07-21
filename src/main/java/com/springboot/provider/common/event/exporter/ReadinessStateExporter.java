package com.springboot.provider.common.event.exporter;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.event.exporter
 * @Author xuzhenkui
 * @Date 2021-05-2516:31
 */
@Component
public class ReadinessStateExporter {

    @EventListener
    public void onStateChange(AvailabilityChangeEvent<ReadinessState> event) {
        switch (event.getState()) {
            case ACCEPTING_TRAFFIC:
                // create file /tmp/healthy
                System.out.println("[ReadinessStateExporter] ACCEPTING_TRAFFIC");
                break;
            case REFUSING_TRAFFIC:
                // remove file /tmp/healthy
                System.out.println("[ReadinessStateExporter] REFUSING_TRAFFIC");
                break;
        }
    }

}
