package com.springboot.provider.module.common.service.impl;

import com.springboot.provider.module.common.service.CommonService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.module.common.service.impl
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-04 14:29
 **/
@Service
public class CommonServiceImpl implements CommonService {

    private final JdbcClient masterJdbcClient;
    private final JdbcClient slaveJdbcClient;

    public CommonServiceImpl(final JdbcClient masterJdbcClient, final JdbcClient slaveJdbcClient) {
        this.masterJdbcClient = masterJdbcClient;
        this.slaveJdbcClient = slaveJdbcClient;
    }

    @Transactional
    @Override
    public Integer insert() {
        final int admin = masterJdbcClient.sql("insert into u1(username,password) values(?,?)").params("admin", "123456").update();
        int i = 1 / 0;
        final int slave = slaveJdbcClient.sql("insert into r1(name,title) values(?,?)").params("ADMIN", "管理员").update();
        return admin + slave;
    }
}
