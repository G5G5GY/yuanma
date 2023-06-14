package com.yuanma.webmvc.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

public class FreemarkerUtil {

    public static String process(String templateContent,Map<String,Object> dataMap){
        try {
            Configuration configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("temp", templateContent);
            configuration.setTemplateLoader(stringLoader);
            Template template = new Template("temp", new StringReader(templateContent),configuration);
            StringWriter stringWriter = new StringWriter();
            template.process(dataMap, stringWriter);
            configuration.setTemplateLoader(stringLoader);
            return stringWriter.toString();
        }catch (Exception e) {
            throw new RuntimeException("模板解析失败");
        }

    }

}
