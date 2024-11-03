package com.local.naruto.common.utils;

import com.local.naruto.common.constant.Constants;
import com.local.naruto.common.exception.ServiceException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * XML工具类
 *
 * @author naruto chen
 * @since 2023-11-25
 */
public class XmlOperationUtils {

    /**
     * 对象转换为XML字符串
     *
     * @param object 对象
     * @return XML字符串
     */
    public static String parseObjectToXmlString(Object object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * 转换为格式化的XML字符串
     *
     * @param object 对象
     * @return XML字符串
     */
    public static String parseObjectToXmlStringFormat(Object object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }

    /**
     * 转换为Java对象
     *
     * @param xmlStr 字符串
     * @param clazz  类型
     * @param <T>    类型
     * @return 对象
     */
    public static <T> T parseXmlToJavaBean(String xmlStr, Class<T> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            T unmarshal = (T) unmarshaller.unmarshal(new StringReader(xmlStr));
            return unmarshal;
        } catch (JAXBException exception) {
            throw new ServiceException(Constants.INT_500, exception.getMessage());
        }
    }
}
