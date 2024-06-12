package com.springboot.provider.common.utils;

import cn.hutool.core.lang.id.NanoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: test
 * @package tools.util
 * @description
 * @author: XuZhenkui
 * @create: 2020-12-16 15:41
 **/
public class JsonAndXmlUtils {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final XmlMapper XML_MAPPER = new XmlMapper();

    static {
        // 对于空的对象转json的时候不抛出错误
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 设置输出时包含属性的风格
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 对于空的对象转json的时候不抛出错误
        XML_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 允许属性名称没有引号
        XML_MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 允许单引号
        XML_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 设置输入时忽略在json字符串中存在但在java对象实际没有的属性
        XML_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        // xml输出节点首字母大写
        XML_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
        // 是否以强制与 Bean 名称自省严格兼容的功能
        XML_MAPPER.configure(MapperFeature.USE_STD_BEAN_NAMING, true);
        // 设置输出时包含属性的风格
        XML_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }


    /**
     * json 转 xml, root为xml根节点
     *
     * @param json
     * @param root
     *
     * @return xml 字符串
     */
    public static String jsonToXml(String json, String root) {
        if (StringUtils.isNotBlank(json)) {
            try {
                final Map map = OBJECT_MAPPER.readValue(json, Map.class);
                final String xml = XML_MAPPER.writeValueAsString(map);
                return xml.replaceFirst("<LinkedHashMap>", "<" + root + ">").replaceFirst("</LinkedHashMap>", "</" + root + ">");
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * xml 转 json
     *
     * @param xml
     *
     * @return json 字符串
     */
    public static String xmlToJson(String xml) {
        if (StringUtils.isNotBlank(xml)) {
            try {
                final Map map = XML_MAPPER.readValue(xml, Map.class);
                return OBJECT_MAPPER.writeValueAsString(map);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * json 转 Map
     *
     * @param json
     *
     * @return Map 对象
     */
    public static Map jsonToMap(String json) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return OBJECT_MAPPER.readValue(json, Map.class);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 对象转字符串
     *
     * @param object
     *
     * @return json 字符串
     */
    public static String objectToJson(Object object) {
        if (object != null) {
            try {
                return OBJECT_MAPPER.writeValueAsString(object);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 对象转xml格式字符串
     *
     * @param data
     *
     * @return xml 字符串
     */
    public static String objectToXml(Object data) {
        if (data != null) {
            try {
                return XML_MAPPER.writeValueAsString(data);
            } catch (Exception ignored) {
            }
        }
        return null;

    }

    /**
     * json字符串转对象
     *
     * @param json  json字符串
     * @param clazz 类
     * @param <T>   泛型
     *
     * @return java 对象
     */
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        if (StringUtils.isNotBlank(json)) {
            try {
                return OBJECT_MAPPER.readValue(json, clazz);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * json字符串转对象数组
     *
     * @param json json字符串
     * @param <T>  泛型
     *
     * @return java 对象
     */
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (StringUtils.isNotBlank(json)) {
            try {

                JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(ArrayList.class, clazz);
                return OBJECT_MAPPER.readValue(json, javaType);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * xml字符串转对象
     *
     * @param xml   xml字符串
     * @param clazz 类
     * @param <T>   泛型
     *
     * @return java 对象
     */
    public static <T> T xmlToObject(String xml, Class<T> clazz) {
        if (StringUtils.isNotBlank(xml)) {
            try {
                return XML_MAPPER.readValue(xml, clazz);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 获取xml字符串中元素标签值
     * <p>xml中元素标签唯一</p>
     *
     * @param xml     响应报文(xml字符串格式)
     * @param element 元素名
     *
     * @return xml字符串中元素标签值
     *
     * @throws Exception
     */
    public static String getXmlSingleElementValue(String xml, String element) {
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(element)) {
            return null;
        }
        // 元素名<ELEMENT key = value ...>(.*)<ELEMENT/>
        StringBuffer regex = new StringBuffer();
        regex.append("<").append(element + ".*").append(">");
        regex.append("(.*)");
        regex.append("</").append(element).append(">");

        String str = "";
        Matcher matcher = Pattern.compile(regex.toString()).matcher(xml);
        while (matcher.find()) {
            str = matcher.group(1);
        }
        return str;
    }

    /**
     * 获取xml字符串中元素标签值
     * <p>xml存在多个该元素标签</p>
     * <p>exmple:<DATA></DATA></p>
     *
     * @param xml     响应报文(xml字符串格式)
     * @param element 元素名
     *
     * @return xml字符串中元素标签列表
     *
     * @throws Exception
     */
    public static List<String> getXmlListElementValue(String xml, String element) {
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(element)) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        // 元素名<ELEMENT key = value ...>([^</ELEMENT>]*)</ELEMENT>
        StringBuffer regex = new StringBuffer();
        regex.append("<").append(element + ".*").append(">");
        regex.append("([^</" + element + ">]*)");
        regex.append("</").append(element).append(">");
        Matcher matcher = Pattern.compile(regex.toString()).matcher(xml);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

    /**
     * 将xml字符串中的节点转为大写字母
     *
     * @param xml
     *
     * @return
     */
    public static String xmlNodeToUpperCase(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String regex = "<(/*[A-Za-z]+/?)>";
        Matcher matcher = Pattern.compile(regex).matcher(xml);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<" + matcher.group(1).toUpperCase() + ">");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将xml字符串中的节点转为小写字母
     *
     * @param xml
     *
     * @return
     */
    public static String xmlNodeToLowerCase(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String regex = "<(/*[A-Za-z]+/?)>";
        Matcher matcher = Pattern.compile(regex).matcher(xml);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "<" + matcher.group(1).toLowerCase() + ">");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将xml字符串中的节点首字母转为大写字母
     *
     * @param xml
     *
     * @return
     */
    public static String xmlNodeFirstLetterToUpperCase(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String regex = "<(/*[A-Za-z]+/?)>";
        Matcher matcher = Pattern.compile(regex).matcher(xml);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String node = matcher.group(1);
            if (!node.startsWith("/")) {
                matcher.appendReplacement(sb, "<" + node.replaceFirst(node.substring(0, 1), node.substring(0, 1).toUpperCase()) + ">");
            } else {
                matcher.appendReplacement(sb, "<" + node.replaceFirst(node.substring(1, 2), node.substring(1, 2).toUpperCase()) + ">");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String xmlNodeFirstLetterToUpper(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String regex = "<(/*[A-Za-z]+/?)>";
        Matcher matcher = Pattern.compile(regex).matcher(xml);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String node = matcher.group(1);
            if (!node.startsWith("/")) {
                matcher.appendReplacement(sb, "<" + (StringUtils.capitalize(node)) + ">");
            } else {
                matcher.appendReplacement(sb, "</" + (StringUtils.capitalize(node.substring(1))) + ">");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将xml字符串中的节点首字母转为小写字母
     *
     * @param xml
     *
     * @return
     */
    public static String xmlNodeFirstLetterToLowerCase(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String regex = "<(/*[A-Za-z]+/?)>";
        Matcher matcher = Pattern.compile(regex).matcher(xml);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String node = matcher.group(1);
            if (!node.startsWith("/")) {
                matcher.appendReplacement(sb, "<" + node.replaceFirst(node.substring(0, 1), node.substring(0, 1).toLowerCase()) + ">");
            } else {
                matcher.appendReplacement(sb, "<" + node.replaceFirst(node.substring(1, 2), node.substring(1, 2).toLowerCase()) + ">");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 大小写字母相互转换
     *
     * @param c
     *
     * @return
     */
    public static char characterConvertor(char c) {
        if ((int) c >= 65 && (int) c <= 90) {
            return (char) (c + 32);
        } else if ((int) c >= 97 && (int) c <= 122) {
            return (char) (c - 32);
        } else {
            return c;
        }
    }

    /**
     * 大写字母转为小写字母
     *
     * @param c 大写字母
     *
     * @return 小写字母
     */
    public static char upperToLowerConvertor(char c) {
        if ((int) c >= 65 && (int) c <= 90) {
            return (char) (c + 32);
        } else {
            return c;
        }
    }

    /**
     * 小写字母转为大写字母
     *
     * @param c 小写字母
     *
     * @return 大写字母
     */
    public static char lowerToUpperConvertor(char c) {
        if ((int) c >= 97 && (int) c <= 122) {
            return (char) (c - 32);
        } else {
            return c;
        }
    }

    public static void main(String[] args) {
        final MsgHeader msgHeader = new MsgHeader("HOL", "ODS_24556", "1");
        final MsgBody msgBody = new MsgBody("782161", "夏秋月", "1", "410935199702151986", "19180899555", new User(2323L, "XQY", "123456"));
        final BsXml<MsgBody> bsXml = new BsXml<>(msgHeader, msgBody, "职工医保");

        // Object 转 xml字符串
        final String xml = JsonAndXmlUtils.objectToXml(bsXml);
        System.out.println(xml);

        // xml字符串 转 Object
        final BsXml xmlToObject = JsonAndXmlUtils.xmlToObject(xml, BsXml.class);
        System.out.println(xmlToObject);

        // xml 转 json
        final String json = JsonAndXmlUtils.xmlToJson(xml);
        System.out.println(json);

        // json 转 xml
        final String XML = JsonAndXmlUtils.jsonToXml(json, "BsXml");
        System.out.println(XML);

        final BsXml xmlObj = JsonAndXmlUtils.xmlToObject(XML, BsXml.class);
        System.out.println(xmlObj);


        String xml2 = "<ROOT>\n" +
                "    <OPSYSTEM>HIS</OPSYSTEM>\n" +
                "    <OPWINID>1</OPWINID>\n" +
                "    <CONSIS_PRESC_MSTVW>\n" +
                "        <PRESC_DATE>2013-09-12 10:19:50</PRESC_DATE>\n" +
                "        <PRESC_NO>1</PRESC_NO>\n" +
                "        <CONSIS_PRESC_DTLVW>\n" +
                "            <PRESC_NO>1-30585836</PRESC_NO>\n" +
                "            <ITEM_NO>1</ITEM_NO>\n" +
                "        </CONSIS_PRESC_DTLVW>\n" +
                "\t\t<CONSIS_PRESC_DTLVW>\n" +
                "            <PRESC_NO>1-30585836</PRESC_NO>\n" +
                "            <ITEM_NO>2</ITEM_NO>\n" +
                "        </CONSIS_PRESC_DTLVW>\n" +
                "    </CONSIS_PRESC_MSTVW>\n" +
                "\t <CONSIS_PRESC_MSTVW>\n" +
                "        <PRESC_DATE>2013-09-12 10:19:50</PRESC_DATE>\n" +
                "        <PRESC_NO>2</PRESC_NO>\n" +
                "        <CONSIS_PRESC_DTLVW>\n" +
                "            <PRESC_NO>1-30585836</PRESC_NO>\n" +
                "            <ITEM_NO>3</ITEM_NO>\n" +
                "        </CONSIS_PRESC_DTLVW>\n" +
                "\t\t<CONSIS_PRESC_DTLVW>\n" +
                "            <PRESC_NO>1-30585836</PRESC_NO>\n" +
                "            <ITEM_NO>4</ITEM_NO>\n" +
                "        </CONSIS_PRESC_DTLVW>\n" +
                "    </CONSIS_PRESC_MSTVW>\n" +
                "</ROOT>";

        ROOT xml1 = JsonAndXmlUtils.xmlToObject(xml2, ROOT.class);
        System.out.println(xml1);

        final ROOT his = new ROOT("HIS", "1", Arrays.asList(new ConsisPrescMstvw("2013-09-12 10:19:50", "2", Arrays.asList(new ConsisPrescDtlvw("1-30585836", "1")))));
        System.out.println(JsonAndXmlUtils.objectToXml(his));

    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class BsXml<T> {
        public MsgHeader msgHeader;
        public T msgBody;
        public String extra;

        @Override
        public String toString() {
            return JsonAndXmlUtils.objectToJson(this);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgHeader {
        public String sender;
        public String msgType;
        public String msgVersion;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgBody {
        public String patientId;
        public String patientName;
        public String identityCardType;
        public String identityCardNumber;
        public String mobile;

        @JacksonXmlProperty(localName = "PatientInfo")
        public User user;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class User implements Cloneable {
        public Long id;
        public String username;
        public String password;

        // 使用 useWrapping 确定输出的集合是否包装或平铺
        @JacksonXmlElementWrapper(localName = "Roles"/*, useWrapping = false*/)
        @JacksonXmlProperty(localName = "Role")
        public List<Role> roles;

        public User(final Long id, final String username, final String password) {
            this.id = id;
            this.username = username;
            this.password = password;
            roles = new ArrayList<>();
            roles.add(new Role(NanoId.randomNanoId(), "USER"));
            roles.add(new Role(NanoId.randomNanoId(), "ADMIN"));
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Role {
        public String code;
        public String name;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    public static class ROOT {
        public String OPSYSTEM;
        public String OPWINID;

        @JacksonXmlElementWrapper(localName = "CONSIS_PRESC_MSTVW", useWrapping = false)
        @JacksonXmlProperty(localName = "CONSIS_PRESC_MSTVW")
        public List<ConsisPrescMstvw> CONSIS_PRESC_MSTVW;

        @Override
        public String toString() {
            return JsonAndXmlUtils.objectToJson(this);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsisPrescMstvw {
        public String PRESC_DATE;
        public String PRESC_NO;

        @JacksonXmlElementWrapper(localName = "CONSIS_PRESC_DTLVW", useWrapping = false)
        @JacksonXmlProperty(localName = "CONSIS_PRESC_DTLVW")
        public List<ConsisPrescDtlvw> CONSIS_PRESC_DTLVW;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsisPrescDtlvw {
        public String PRESC_NO;
        public String ITEM_NO;
    }
}
