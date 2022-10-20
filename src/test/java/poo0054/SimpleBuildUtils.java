package poo0054;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import poo0054.constant.FileConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/20 10:39
 */
public class SimpleBuildUtils {
    /**
     * 防止每次调用都会创建
     */
    private static XPathParser xPathParser;

    public static Configuration buildConfiguration() {
        return new Configuration();
    }

    /**
     * 获取最顶级的 XPathParser
     *
     * @return node
     */
    public static XPathParser buildConfigXpath() {
        if (null != xPathParser) {
            return xPathParser;
        }
        try (InputStream inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig)) {
            return new XPathParser(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getVariables() {
        if (null == xPathParser) {
            xPathParser = buildConfigXpath();
        }
        return propertiesElement(xPathParser.evalNode("/configuration/properties"));
    }


    private static Properties propertiesElement(XNode context) {
        if (context != null) {
            Properties defaults = context.getChildrenAsProperties();
            String resource = context.getStringAttribute("resource");
            String url = context.getStringAttribute("url");
            if (resource != null && url != null) {
                throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
            }
            try {
                if (resource != null) {
                    defaults.putAll(Resources.getResourceAsProperties(resource));
                } else if (url != null) {
                    defaults.putAll(Resources.getUrlAsProperties(url));
                }
            } catch (IOException e) {
                throw new BuilderException("构建Properties失败", e);
            }
            return defaults;
        }
        return null;
    }
}