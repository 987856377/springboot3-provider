package com.springboot.provider.module.lis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.springboot.provider.common.annotation.EncryptField;

import java.io.Serial;

/**
 * <p>
 *
 * </p>
 *
 * @author XuZhenkui
 * @since 2020-12-10
 */
public class Role extends Model<Role> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @EncryptField
    private String title;

    public Role() {
    }

    public Role(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public Role(Long id, String name, String title) {
        this.id = id;
        this.name = name;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name=" + name +
                ", title=" + title +
                "}";
    }
}
