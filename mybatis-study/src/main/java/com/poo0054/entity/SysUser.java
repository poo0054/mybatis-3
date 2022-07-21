/**
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

import java.util.Date;
import java.io.Serializable;

/**
 * (SysUser)实体类
 *
 * @author zhangzhi
 * @since 2022-07-15 15:44:16
 */
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

  private Date createtime;

  private Long updateuserid;

  private Date updatetime;

  private Integer telvalidate;

  private String onlineid;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLoginname() {
    return loginname;
  }

  public void setLoginname(String loginname) {
    this.loginname = loginname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCustomercode() {
    return customercode;
  }

  public void setCustomercode(String customercode) {
    this.customercode = customercode;
  }

  public String getCompanycode() {
    return companycode;
  }

  public void setCompanycode(String companycode) {
    this.companycode = companycode;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWxopenid() {
    return wxopenid;
  }

  public void setWxopenid(String wxopenid) {
    this.wxopenid = wxopenid;
  }

  public Date getLastlogintime() {
    return lastlogintime;
  }

  public void setLastlogintime(Date lastlogintime) {
    this.lastlogintime = lastlogintime;
  }

  public Integer getCompanyadmin() {
    return companyadmin;
  }

  public void setCompanyadmin(Integer companyadmin) {
    this.companyadmin = companyadmin;
  }

  public Integer getEnablestatus() {
    return enablestatus;
  }

  public void setEnablestatus(Integer enablestatus) {
    this.enablestatus = enablestatus;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Long getCreateuserid() {
    return createuserid;
  }

  public void setCreateuserid(Long createuserid) {
    this.createuserid = createuserid;
  }

  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public Long getUpdateuserid() {
    return updateuserid;
  }

  public void setUpdateuserid(Long updateuserid) {
    this.updateuserid = updateuserid;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public Integer getTelvalidate() {
    return telvalidate;
  }

  public void setTelvalidate(Integer telvalidate) {
    this.telvalidate = telvalidate;
  }

  public String getOnlineid() {
    return onlineid;
  }

  public void setOnlineid(String onlineid) {
    this.onlineid = onlineid;
  }

  @Override
  public String toString() {
    return "SysUser{" +
      "id=" + id +
      ", loginname='" + loginname + '\'' +
      ", password='" + password + '\'' +
      ", customercode='" + customercode + '\'' +
      ", companycode='" + companycode + '\'' +
      ", username='" + username + '\'' +
      ", tel='" + tel + '\'' +
      ", email='" + email + '\'' +
      ", wxopenid='" + wxopenid + '\'' +
      ", lastlogintime=" + lastlogintime +
      ", companyadmin=" + companyadmin +
      ", enablestatus=" + enablestatus +
      ", remark='" + remark + '\'' +
      ", createuserid=" + createuserid +
      ", createtime=" + createtime +
      ", updateuserid=" + updateuserid +
      ", updatetime=" + updatetime +
      ", telvalidate=" + telvalidate +
      ", onlineid='" + onlineid + '\'' +
      '}';
  }
}

