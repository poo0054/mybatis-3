/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.builder.xml.test;

import com.poo0054.constant.FileConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author ZhangZhi
 * @version 1.0
 * @date 2022/10/17 16:15
 */
@Slf4j
@ExtendWith({MockitoExtension.class})
public class XMLMapperBuilderTest {

  private InputStream inputStream;
  private Configuration configuration;
  XNode mappers;
  XMLMapperBuilder xmlMapperBuilder;

  @BeforeEach
  void before() throws IOException {
    this.inputStream = Resources.getResourceAsStream(FileConstant.mybatisConfig);
    configuration = new Configuration();
    configuration.setVariables(Resources.getResourceAsProperties("db.properties"));
    XPathParser xPathParser = new XPathParser(inputStream);
    XNode xNode = xPathParser.evalNode("/configuration");
    mappers = xNode.evalNode("mappers");
  }

  @Test
  void mapperElement() throws IOException, ClassNotFoundException {
    for (XNode child : mappers.getChildren()) {
      String resource = child.getStringAttribute("resource");
      String url = child.getStringAttribute("url");
      String mapperClass = child.getStringAttribute("class");

      //url和resource使用XMLMapperBuilder
      if (StringUtils.isNotEmpty(resource)) {
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
          XPathParser mapperXml = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
          xmlMapperBuilder = new XMLMapperBuilder(mapperXml, configuration, resource, configuration.getSqlFragments());
//          xmlMapperBuilder.parse();
          configurationElement(mapperXml);
        }
      }
      //mapperClass和package使用mapperRegistry
      if (StringUtils.isNotEmpty(mapperClass)) {
        // 如果 <mapper>节点 指定了 class属性，则向 MapperRegistry 注册 该Mapper接口
        Class<?> mapperInterface = Resources.classForName(mapperClass);
        configuration.addMapper(mapperInterface);
      }
    }
  }

  void configurationElement(XPathParser mapperXml) {
    //解析
    XNode mapper = mapperXml.evalNode("/mapper");
    String namespace = mapper.getStringAttribute("namespace");
    xmlMapperBuilder.getBuilderAssistant().setCurrentNamespace(namespace);
    //构建cache-ref
    cacheRefElement(mapper.evalNode("cache-ref"));

    //使用CacheBuilder创建cache
    cacheElement(mapper.evalNode("cache"));
    //参数map解析
    xmlMapperBuilder.parameterMapElement(mapper.evalNodes("/mapper/parameterMap"));

    //resultMap 解析
    List<XNode> xNodes = mapper.evalNodes("/mapper/resultMap");
    for (XNode resultMapNode : xNodes) {
      try {
        //处理
        xmlMapperBuilder.resultMapElement(resultMapNode, Collections.emptyList(), null);
      } catch (IncompleteElementException e) {
        // ignore, it will be retried
      }
    }
  }


  void cacheElement(XNode cacheNode) {
    if (cacheNode != null) {
      String type = cacheNode.getStringAttribute("type", "PERPETUAL");
      //使用别名或者全类名
      TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
      Class<? extends Cache> typeClass = typeAliasRegistry.resolveAlias(type);
      String eviction = cacheNode.getStringAttribute("eviction", "LRU");

      Class<? extends Cache> evictionClass = typeAliasRegistry.resolveAlias(eviction);
      Long flushInterval = cacheNode.getLongAttribute("flushInterval");
      Integer size = cacheNode.getIntAttribute("size");
      boolean readWrite = !cacheNode.getBooleanAttribute("readOnly", false);
      boolean blocking = cacheNode.getBooleanAttribute("blocking", false);
      Properties props = cacheNode.getChildrenAsProperties();
      //把准备好的cache放入BuilderAssistant中
      xmlMapperBuilder.getBuilderAssistant().useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
    }
  }

  void cacheRefElement(XNode cacheRef) {
    if (cacheRef != null) {
      // key：当前Namespace value：引用的Namespace
      MapperBuilderAssistant builderAssistant = xmlMapperBuilder.getBuilderAssistant();
      configuration.addCacheRef(builderAssistant.getCurrentNamespace(), cacheRef.getStringAttribute("namespace"));
      // context.getStringAttribute("namespace") 引用的namespace
      CacheRefResolver cacheRefResolver = new CacheRefResolver(builderAssistant, cacheRef.getStringAttribute("namespace"));
      try {
        //如果出现异常  就放入未完成的config中 等待下次使用session进行数据库操作的时候加载
        cacheRefResolver.resolveCacheRef();
      } catch (IncompleteElementException e) {
        configuration.addIncompleteCacheRef(cacheRefResolver);
      }
    }
  }
}
