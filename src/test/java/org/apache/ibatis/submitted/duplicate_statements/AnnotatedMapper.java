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
package org.apache.ibatis.submitted.duplicate_statements;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * This interface should be OK.  It has duplicate method names, but only
 * because of the RowBounds parameter
 */
public interface AnnotatedMapper {

  @Select("select * from users")
  List<User> getAllUsers();

  @Select("select * from users")
  List<User> getAllUsers(RowBounds rowBounds);
}
