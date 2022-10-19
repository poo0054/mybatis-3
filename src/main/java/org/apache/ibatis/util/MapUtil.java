/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.util;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class MapUtil {
  /**
   * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
   * This class should be removed once we drop Java 8 support.
   * 针对 Java 8 特定性能问题 JDK-8161372 的临时解决方法。<br> 一旦我们放弃对 Java 8 的支持，就应该删除这个类。
   * <p>
   * map中的key不为空直接返回，为空就调用computeIfAbsent方法 ——> key不存在 就调用Function并且不为空就添加入map
   *
   * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
   */
  public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<K, V> mappingFunction) {
    V value = map.get(key);
    if (value != null) {
      return value;
    }
    return map.computeIfAbsent(key, mappingFunction);
  }

  /**
   * Map.entry(key, value) alternative for Java 8.
   */
  public static <K, V> Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleImmutableEntry<>(key, value);
  }

  private MapUtil() {
    super();
  }
}
