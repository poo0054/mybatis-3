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
package com.poo0054.dao;

import com.poo0054.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.awt.print.Pageable;
import java.util.List;

/**
 * (SysUser)表数据库访问层
 *
 * @author zhangzhi
 * @since 2022-07-15 15:45:55
 */
public interface SysUserDao {

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  SysUser queryById(Long id);

  /**
   * 查询指定行数据
   *
   * @param sysUser  查询条件
   * @param pageable 分页对象
   * @return 对象列表
   */
  List<SysUser> queryAllByLimit(SysUser sysUser, @Param("pageable") Pageable pageable);

  /**
   * 统计总行数
   *
   * @param sysUser 查询条件
   * @return 总行数
   */
  long count(SysUser sysUser);

  /**
   * 新增数据
   *
   * @param sysUser 实例对象
   * @return 影响行数
   */
  int insert(SysUser sysUser);

  /**
   * 批量新增数据（MyBatis原生foreach方法）
   *
   * @param entities List<SysUser> 实例对象列表
   * @return 影响行数
   */
  int insertBatch(@Param("entities") List<SysUser> entities);

  /**
   * 批量新增或按主键更新数据（MyBatis原生foreach方法）
   *
   * @param entities List<SysUser> 实例对象列表
   * @return 影响行数
   * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
   */
  int insertOrUpdateBatch(@Param("entities") List<SysUser> entities);

  /**
   * 修改数据
   *
   * @param sysUser 实例对象
   * @return 影响行数
   */
  int update(SysUser sysUser);

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 影响行数
   */
  int deleteById(Long id);

}

