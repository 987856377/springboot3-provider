package com.springboot.provider.common.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.client.Client;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

public class WebServiceUtils {
    private static final Log LOGGER = LogFactory.getLog(WebServiceUtils.class);

    private static final String WEBSERVICE_INVOKE_URL = "http://10.80.5.34:9528/hai/WebServiceEntry?wsdl";

    public static String getPlatform(String service, String xml) {
        LOGGER.info(" >>> 调用 WebService 接口：service: " + service + "; 入参: " + xml);
        String reStr = "";
        try {
            Client client = new Client(new URL(WEBSERVICE_INVOKE_URL));
            client.setUrl(WEBSERVICE_INVOKE_URL);
            Object[] result = client.invoke("invoke", new Object[]{service, "HOL", "HOL", xml});

//            String expression = "/BSXml/MsgBody/Detail/text()";
//            String content = getReXml((String) result[0], expression);

            reStr = (String) result[0];
            client.close();
        } catch (Exception e) {
            LOGGER.error(" >>> 调用 WebService 接口：service: " + service + ", 异常" + e.getMessage());
        }
        LOGGER.info(" >>> 调用 WebService 接口：service: " + service + "; 出参: " + reStr);
        return reStr;
    }

    public static String getReXml(String xml, String expression) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        InputSource is = new InputSource(new StringReader(xml));
        XPath xPath = XPathFactory.newInstance().newXPath();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(is);
        return (String) xPath.evaluate(expression, document, XPathConstants.STRING);
    }

}
