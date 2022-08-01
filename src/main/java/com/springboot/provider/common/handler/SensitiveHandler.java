package com.springboot.provider.common.handler;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.StringTypeHandler;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description
 * @Project springboot3-provder
 * @Package com.springboot.provider.common.handler
 * @Author xuzhenkui
 * @Date 2022-07-28 23:18
 */
public class SensitiveHandler extends StringTypeHandler {
    private final SM4 sm4 = SmUtil.sm4("Sm4NativeEncrypt".getBytes(StandardCharsets.UTF_8));

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StringUtils.hasText(parameter)) {
            ps.setString(i, sm4.encryptBase64(parameter));
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (StringUtils.hasText(rs.getString(columnName))) {
            return sm4.decryptStr(rs.getString(columnName));
        }
        return null;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (StringUtils.hasText(rs.getString(columnIndex))) {
            return sm4.decryptStr(rs.getString(columnIndex));
        }
        return null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (StringUtils.hasText(cs.getString(columnIndex))) {
            return sm4.decryptStr(cs.getString(columnIndex));
        }
        return null;
    }
}
