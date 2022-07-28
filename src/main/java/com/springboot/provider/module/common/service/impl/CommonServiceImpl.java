package com.springboot.provider.module.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.springboot.provider.module.common.service.CommonService;
import com.springboot.provider.module.his.entity.User;
import com.springboot.provider.module.his.service.UserService;
import com.springboot.provider.module.lis.entity.Role;
import com.springboot.provider.module.lis.service.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @program: springboot-provider
 * @package com.springboot.provider.module.common.service.impl
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-04 14:29
 **/
@Service
public class CommonServiceImpl implements CommonService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    public CommonServiceImpl(PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @DSTransactional
    @Override
    public Integer insert() {
        String username = UUID.randomUUID().toString();
        username = username.substring(0, 16);

        User user = new User();
//        user.setId(SnowflakeConstants.next());
        user.setUsername(username);
        user.setPassword("username");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int his = userService.insert(user);
//        int a= 1/0;

        Role role = new Role();
//        role.setId(SnowflakeConstants.next());
        role.setName("admin");
        role.setTitle("ADMIN");
        int lis = roleService.insert(role);

//        int i = 1/0;
        return his + lis;
    }

}
