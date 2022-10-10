package com.springboot.provider.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.springboot.provider.common.BaseEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Description
 * @Project springboot-provider
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2021/11/22 14:44
 */
@Component
public class MyBatisMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject != null && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

//            baseEntity.setCreateUser();
            baseEntity.setCreateTime(LocalDateTime.now());
            baseEntity.setDeleteFlag(0);
            baseEntity.setVersion(1);
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject != null && metaObject.getOriginalObject() instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();

//            baseEntity.setUpdateUser();
            baseEntity.setUpdateTime(LocalDateTime.now());
        }
    }
}
