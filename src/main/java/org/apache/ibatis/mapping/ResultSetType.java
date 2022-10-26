/**
 * Copyright 2009-2022 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.mapping;

import java.sql.ResultSet;

/**
 * @author Clinton Begin
 */
public enum ResultSetType {
    /**
     * 指示其光标只能向前移动的ResultSet对象类型的常量。。
     */
    FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    /**
     * 指示可滚动但通常对ResultSet基础数据的更改不敏感的ResultSet对象类型的常量。
     */
    SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    /**
     * 指示可滚动的ResultSet对象类型的常量，通常对ResultSet下面的数据的更改敏感。
     */
    SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    private final int value;

    ResultSetType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
