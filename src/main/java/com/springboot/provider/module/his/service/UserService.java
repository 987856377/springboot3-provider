package com.springboot.provider.module.his.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.provider.module.his.entity.User;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author XuZhenkui
 * @since 2020-12-10
 */
public interface UserService extends IService<User> {
    List<User> getAllUser();

    User getByUserId(Long id);

    Integer insert(User user);

    Integer insertBatchSomeColumn(Collection<User> entityList);
}
