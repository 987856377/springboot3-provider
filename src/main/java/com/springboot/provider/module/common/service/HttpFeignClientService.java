package com.springboot.provider.module.common.service;

import com.springboot.provider.common.ResultJson;
import com.springboot.provider.module.his.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "httpFeignClientService", url = "http://127.0.0.1:8080")
public interface HttpFeignClientService {

    @RequestMapping("/batch/job/batchDataSourceItemReaderJob")
    String batchDataSourceItemReaderJob();

    @RequestMapping("/batch/user")
    ResultJson<User> getUser(@RequestBody User user);
}
