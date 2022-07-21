package com.springboot.provider.module.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.springboot.mjt.proxy.JdbcOperationsProxy;
import com.springboot.mjt.proxy.NamedParameterJdbcOperationsProxy;
import com.springboot.provider.common.ResultJson;
import com.springboot.provider.common.constants.SnowflakeConstants;
import com.springboot.provider.mjt.constants.Mapper;
import com.springboot.provider.module.his.entity.User;
import com.springboot.provider.module.lis.entity.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class MjtController {

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/sql")
    public ResultJson sql() {
        JdbcOperations hisJdbcOperations = JdbcOperationsProxy.getProxyInstance("hisDataSource");
        JdbcOperations lisJdbcOperations = JdbcOperationsProxy.getProxyInstance("lisDataSource");

//        int saveUser = hisJdbcOperations.update(Mapper.saveUser, "xzk", "123456", new Date(), 1);
////        int a = 1 / 0;
//        int saveRole = lisJdbcOperations.update(Mapper.saveRole, "auditor", "AUDITOR");
////        int b = 1 / 0;

//        String title = lisJdbcOperations.queryForObject(Mapper.getRoleById, String.class, 1);

//        Map<String, Object> map = lisJdbcOperations.queryForMap(Mapper.getRoleById, 1);

//        List<Map<String, Object>> maps = lisJdbcOperations.queryForList(Mapper.getRoleById, 1);

        List<Role> roles = lisJdbcOperations.query(Mapper.selectById, new BeanPropertyRowMapper<>(Role.class), 1, "ADMIN");

//        RowMapper<Role> rowMapper = new BeanPropertyRowMapper<>(Role.class);
////        List<Role> roles = jdbcTemplate.query("select * from role", rowMapper);
//        List<Role> roles = lisJdbcOperations.query(Mapper.selectById, rowMapper, 1, "ADMIN");

//        List<Object[]> list = new ArrayList<>();
//        list.add(new Object[]{"admin", "ADMIN"});
//        list.add(new Object[]{"dba", "DBA"});
//        int[] ints = lisJdbcOperations.batchUpdate("insert into role (name, title) values (?, ?)", list);

//        List<Role> roleList = new ArrayList<>();
//        roleList.add(new Role("admin", "ADMIN"));
//        roleList.add(new Role("dba", "DBA"));
//        int[][] ints = lisJdbcOperations.batchUpdate("insert into role (name, title) values (?, ?)", roleList, roleList.size(), (preparedStatement, role) -> {
//            preparedStatement.setString(1, role.getName());
//            preparedStatement.setString(2, role.getTitle());
//        });

        return ResultJson.success(roles);
    }

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping("/name")
    public ResultJson name() throws JsonProcessingException {

//        NamedParameterJdbcOperations namedParameterJdbcTemplate = NamedParameterJdbcOperationsProxy.getProxyInstance(DataSourceFactory.getDataSource("hisDataSource"));
//////        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(DataSourceFactory.getDataSource("hisDataSource"));
//////        List<User> query = namedParameterJdbcTemplate.query("select * from user limit 1", new BeanPropertyRowMapper<>(User.class));
////
//        ObjectMapper objectMapper = new ObjectMapper();
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", 1);
//        map.put("name", "dsd");
//
////        BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(query.get(0));
//
//        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
//        mapSqlParameterSource.addValues(map);
//        mapSqlParameterSource.addValue("age", 12);
//
//        User user = new User();
//        user.setId(1L);
//        BeanPropertySqlParameterSource beanPropertySqlParameterSource1 = new BeanPropertySqlParameterSource(user);
////        List<User> query1 = namedParameterJdbcTemplate.query(Mapper.getUserById, beanPropertySqlParameterSource1, new BeanPropertyRowMapper<>(User.class));
//
//        Map parmas = ImmutableMap.of("id",1L);
//        List query = namedParameterJdbcTemplate.query("select * from user where id = :id", parmas, new BeanPropertyRowMapper<>(User.class));

        NamedParameterJdbcOperations namedParameterJdbcTemplate = NamedParameterJdbcOperationsProxy.getProxyInstance("hisDataSource");

        Map parmas = ImmutableMap.of("id", 1L);

        User user = new User();
        user.setId(1L);
        BeanPropertySqlParameterSource beanPropertySqlParameterSource1 = new BeanPropertySqlParameterSource(user);

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(parmas);

        List query = namedParameterJdbcTemplate.query(Mapper.getUserById, mapSqlParameterSource, new BeanPropertyRowMapper<>(User.class));

        User user1 = new User();
        user1.setId(SnowflakeConstants.next());
        user1.setUsername("xzk1");
        user1.setPassword("123");
        user1.setStatus(1);
        User user2 = new User();
        user2.setId(SnowflakeConstants.next());
        user2.setUsername("xzk2");
        user2.setPassword("123");
        user2.setStatus(1);

        // List<BeanPropertySqlParameterSource> userList = new ArrayList<>();
        // userList.add(new BeanPropertySqlParameterSource(user1));
        // userList.add(new BeanPropertySqlParameterSource(user2));
        // namedParameterJdbcTemplate.batchUpdate(Mapper.batchSaveUser, userList.toArray(new BeanPropertySqlParameterSource[0]));

        List<User> userList1 = new ArrayList<>();
        userList1.add(user1);
        userList1.add(user2);
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(userList1);
        namedParameterJdbcTemplate.batchUpdate(Mapper.batchSaveUser, batch);

        return ResultJson.success(query);
    }

}
