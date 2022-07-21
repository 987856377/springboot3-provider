package com.springboot.provider.common.lifecycle;

import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2022-07-20 14:40
 */
public class AutoConfigurationImportListenerHandler implements AutoConfigurationImportListener {

    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {
        System.out.println("[AutoConfigurationImportListener] onAutoConfigurationImportEvent");
    }
}
