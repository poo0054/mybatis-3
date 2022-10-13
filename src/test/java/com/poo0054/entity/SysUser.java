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
package com.poo0054.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (SysUser)实体类
 *
 * @author zhangzhi
 * @since 2022-07-15 15:44:16
 */
@Data
public class SysUser implements Serializable {
  private static final long serialVersionUID = -90027834368626026L;

  private Long id;

  private String loginname;

  private String password;

  private String customercode;

  private String companycode;

  private String username;

  private String tel;

  private String email;

  private String wxopenid;

  private Date lastlogintime;

  private Integer companyadmin;

  private Integer enablestatus;

  private String remark;

  private Long createuserid;

  private String createtime;

  private Long updateuserid;

  private Date updatetime;

  private Integer telvalidate;

  private String onlineid;


}

