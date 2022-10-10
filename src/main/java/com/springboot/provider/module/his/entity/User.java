package com.springboot.provider.module.his.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.springboot.provider.common.BaseEntity;
import com.springboot.provider.common.handler.SensitiveHandler;
import com.springboot.provider.common.jackson.Sensitive;
import com.springboot.provider.common.jackson.SensitiveMode;
import jakarta.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author XuZhenkui
 * @since 2020-12-10
 */
@TableName(value = "user", autoResultMap = true)
public class User extends BaseEntity<User> {

    @Sensitive(maskFunc = SensitiveMode.MID)
    @TableField("username")
    @NotNull(message = "用户名不能为空")
    private String username;

    @TableField(value = "password", typeHandler = SensitiveHandler.class)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                "} " + super.toString();
    }
}
