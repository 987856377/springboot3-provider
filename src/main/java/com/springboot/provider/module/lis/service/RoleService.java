package com.springboot.provider.module.lis.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.provider.module.lis.entity.Role;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author XuZhenkui
 * @since 2020-12-10
 */
@DS("slave")
public interface RoleService extends IService<Role> {
    List<Role> getRoleList();

    Integer insert(Role role);
}
