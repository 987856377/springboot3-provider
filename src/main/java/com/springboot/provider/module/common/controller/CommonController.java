package com.springboot.provider.module.common.controller;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.util.concurrent.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.springboot.provider.common.ResultCode;
import com.springboot.provider.common.ResultJson;
import com.springboot.provider.common.annotation.OptionLog;
import com.springboot.provider.common.enums.DataSourceEnum;
import com.springboot.provider.common.event.ApplicationMessageEvent;
import com.springboot.provider.common.event.ApplicationNotifyEvent;
import com.springboot.provider.common.holder.ApplicationContextDataSourceHolder;
import com.springboot.provider.common.holder.CallbackThreadPoolExecutorHolder;
import com.springboot.provider.common.proxy.JdbcOperationsProxy;
import com.springboot.provider.common.spi.demo.algorithm.AlgorithmConfiguration;
import com.springboot.provider.common.spi.demo.algorithm.encrypt.EncryptAlgorithm;
import com.springboot.provider.common.spi.demo.algorithm.encrypt.context.EncryptContext;
import com.springboot.provider.common.spi.demo.algorithm.encrypt.factory.EncryptAlgorithmFactory;
import com.springboot.provider.common.spi.demo.compress.Compressor;
import com.springboot.provider.common.spi.simple.agent.AgentTypedSPIRegistry;
import com.springboot.provider.common.utils.PropertyUtils;
import com.springboot.provider.common.utils.ResourceUtils;
import com.springboot.provider.module.common.AppPayProperties;
import com.springboot.provider.module.common.service.CommonService;
import com.springboot.provider.module.common.service.PayService;
import com.springboot.provider.module.his.entity.User;
import com.springboot.provider.module.lis.entity.Role;
import com.springboot.provider.module.pay.enums.PayStrategy;
import com.springboot.provider.module.pay.factory.PayStrategyFactory;
import jakarta.servlet.ServletContext;
import jakarta.websocket.server.PathParam;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CommonController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RateLimiter rateLimiter = RateLimiter.create(5.0);

    private final BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100);


    private final AtomicLong counter = new AtomicLong();

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ServletContext servletContext;

    private final CommonService commonService;

    private final RestTemplate restTemplate;

    private final PayService payService;

//    private final Compressor compressor;

    public CommonController(ApplicationEventPublisher applicationEventPublisher, ServletContext servletContext,
                            CommonService commonService, RestTemplate restTemplate, PayService payService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.servletContext = servletContext;
        this.commonService = commonService;
        this.restTemplate = restTemplate;
        this.payService = payService;
    }

    /*
     * @cache（“something");这个相当于save（）操作，
     * @cachePut相当于Update（）操作，只要他标示的方法被调用，那么都会缓存起来，而@cache则是先看下有没已经缓存了，然后再选择是否执行方法。
     * @CacheEvict相当于Delete（）操作。用来清除缓存用的。
     * */

    @Cacheable("index")
    @RequestMapping("/common")
    public ResultJson index() {
        User user = new User();
        user.setUsername("spring boot");
        user.setPassword((String.valueOf(counter.incrementAndGet())));

        logger.info("servletContext.getContextPath() = " + servletContext.getContextPath());

        logger.info("loadProperties: " + PropertyUtils.loadProperties("application.properties").getProperty("context.initializer.classes"));
        logger.info("loadProperties: " + PropertyUtils.loadAbsolutePathProperties("D:\\IdeaProjects\\development\\src\\main\\resources\\application.properties").getProperty("server.port"));

        String getCost = ResourceUtils.getResource(null, "db/quartz_mysql.sql");
        return ResultJson.success(getCost);
    }

    @RequestMapping("/user")
    public ResultJson user() {
        applicationEventPublisher.publishEvent(new ApplicationNotifyEvent(counter, true));
        applicationEventPublisher.publishEvent(new ApplicationMessageEvent("用户消息发送成功!"));
        return ResultJson.success("Greetings user from Spring Boot! " + counter.incrementAndGet());
    }

    @RequestMapping("/admin")
    public ResultJson admin() {
        return ResultJson.success("Greetings admin from Spring Boot! " + counter.incrementAndGet());
    }

    @RequestMapping("/test/addDataSource")
    public ResultJson addDataSource() {
        DataSource build = DataSourceBuilder.create()
                .type(MysqlDataSource.class)
                .url("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false")
                .username("root")
                .password("root").build();

        DataSource build1 = ApplicationContextDataSourceHolder.builder()
                .jdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&useSSL=false")
                .username("root")
                .password("root").build();


        DataSource dataSource = ApplicationContextDataSourceHolder.buildDataSource(DataSourceEnum.MYSQL.getDbType(), "localhost", "3306", "test", "root", "root", "");

        ApplicationContextDataSourceHolder.addDataSource("test", build1);
        return ResultJson.success();
    }

    @RequestMapping("/test/getRunningSQL")
    public ResultJson getRunningSQL() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(ApplicationContextDataSourceHolder.getDataSource("test")));
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM information_schema.processlist WHERE STATE = 'Sending data'");
        return ResultJson.success(maps);
    }

    @RequestMapping("/test/getDataSource")
    public ResultJson getFromDataSource() {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(Objects.requireNonNull(MultiDataSourceHolder.getDataSource("development")));
        JdbcOperations jdbcTemplate = JdbcOperationsProxy.getProxyInstance("test");

//        List<Object[]> list = new ArrayList<>();
//        list.add(new Object[]{"admin", "ADMIN"});
//        list.add(new Object[]{"dba", "DBA"});
//        int[] ints = jdbcTemplate.batchUpdate("insert into role (name, title) values (?, ?)", list);

//        List<Role> roleList = new ArrayList<>();
//        roleList.add(new Role("admin", "ADMIN"));
//        roleList.add(new Role("dba", "DBA"));
//        int[][] ints = jdbcTemplate.batchUpdate("insert into role (name, title) values (?, ?)", roleList, roleList.size(), (preparedStatement, role) -> {
//            preparedStatement.setString(1, role.getName());
//            preparedStatement.setString(2, role.getTitle());
//        });

        RowMapper<Role> rowMapper = new BeanPropertyRowMapper<>(Role.class);
//        List<Role> roles = jdbcTemplate.query("select * from role", rowMapper);
        List<Role> roles = jdbcTemplate.query("select * from role where id = ? and code = ?", rowMapper, 1, "admin");

//        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from role");
//        List<String> nameList = jdbcTemplate.queryForList("select name from role", String.class);
//        List<String> nameList = jdbcTemplate.queryForList("select name from role where id >= ?", String.class, 9);

//        String name = jdbcTemplate.queryForObject("select name from role where id = ?", String.class, 1);


        return ResultJson.success(roles);
    }

    @RequestMapping("/test/insert")
    public ResultJson insert() {
        return ResultJson.success(commonService.insert());
    }

    @RequestMapping("/test/pay/{type}")
    public String payPath(@PathVariable("type") String type) {
        PayStrategyFactory.get(PayStrategy.getEnumByKey(type)).pay();
        return Objects.requireNonNull(PayStrategy.getEnumByKey(type)).toString();
    }

    @RequestMapping("/test/pay")
    public String payForm(@PathParam("type") String type) {
        PayStrategyFactory.get(PayStrategy.getEnumByKey(type)).pay();
        return Objects.requireNonNull(PayStrategy.getEnumByKey(type)).toString();
    }

    @RequestMapping("/test/payProperty")
    public String payProperty() {
        return payService.pay();
    }

    @OptionLog(mode = 1, source = "api")
    @RequestMapping(value = "/test/xml", method = RequestMethod.POST, produces = {"application/xml;charset=utf-8"})
    public String xml(@RequestBody String xml) {
        return xml;
    }

    @RequestMapping("/test/async")
    public String async() {

        ListenableFuture<Object> submit = CallbackThreadPoolExecutorHolder.getThreadPoolExecutor().submit(() -> {
            logger.info("submit: " + Thread.currentThread().getName());
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            logger.info(LocalDateTime.now().toString());
//            throw new RuntimeException("error");

            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("https://baidu.com", null, String.class);
            return stringResponseEntity;
        });

        Futures.addCallback(submit, new FutureCallback<Object>() {
            @Override
            public void onSuccess(@Nullable Object result) {
                logger.info("onSuccess: " + Thread.currentThread().getName());
                logger.info("result = " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                logger.info("onFailure: " + Thread.currentThread().getName());
                logger.info("t.getMessage() = " + t.getMessage());
            }
        }, MoreExecutors.directExecutor());

        return "success";
    }

    @RequestMapping(value = "/test/limit/{id}")
    public ResultJson limit(@PathVariable String id) {
        if (rateLimiter.tryAcquire(1)) {
            boolean b = bloomFilter.mightContain(1);
            return ResultJson.success("consume: " + id + " success, Container might contain 1: " + b);
        }
        return ResultJson.failure(ResultCode.SERVICE_UNAVAILABLE, "access too frequently");
    }

    @RequestMapping(value = "/test/spi/factory/{param}")
    public ResultJson factory(@PathVariable String param) {
        List<Compressor> compressors = SpringFactoriesLoader.loadFactories(Compressor.class, null);
        for (Compressor compressor : compressors) {
            if ("GZIP".equals(compressor.getType())) {
                byte[] compress = compressor.compress(param.getBytes(StandardCharsets.UTF_8));
                return ResultJson.success(new String(compress));
            }
        }
        return null;
    }

    @RequestMapping(value = "/test/spi/{param}")
    public ResultJson spi(@PathVariable String param) {
        Compressor compressor = AgentTypedSPIRegistry.getRegisteredService(Compressor.class, "GZIP");
        byte[] compress = compressor.compress(param.getBytes(StandardCharsets.UTF_8));
        return ResultJson.success(new String(compress));
    }

    @RequestMapping(value = "/test/spi/algorithm/{type}/{param}")
    public ResultJson algorithm(@PathVariable String type, @PathVariable String param) {
        EncryptAlgorithm<Object, String> algorithm = EncryptAlgorithmFactory.newInstance(new AlgorithmConfiguration("SM4", createECBProperties()));
        EncryptContext encryptContext = new EncryptContext("test", "test", "test", "name");
        return ResultJson.success("enc".equals(type) ? algorithm.encrypt(param, encryptContext) : algorithm.decrypt(param, encryptContext));
    }

    @RequestMapping(value = "/test/javassist")
    public ResultJson javassist(@RequestParam("a") int a, @RequestParam("b") int b) {
        try {
            AppPayProperties obj = new AppPayProperties();

//            // 调用display方法
            Method display = AppPayProperties.class.getDeclaredMethod("display");
            display.invoke(obj);

            // 调用calc方法
            Method calc = AppPayProperties.class.getDeclaredMethod("calc", int.class, int.class);
            Object result = calc.invoke(obj, a, b);

            return ResultJson.success(result);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ResultJson.success();
    }

    private Properties createECBProperties() {
        Properties result = new Properties();
        result.setProperty("sm4-key", "4D744E003D713D054E7E407C350E447E");
        result.setProperty("sm4-mode", "ECB");
        result.setProperty("sm4-padding", "PKCS5Padding");
        return result;
    }

}
