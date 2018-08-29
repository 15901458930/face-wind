package com.xxl.wechat.model.generator.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSyMainCategory<M extends BaseSyMainCategory<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("ID", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("ID");
	}

	public void setName(java.lang.String name) {
		set("NAME", name);
	}
	
	public java.lang.String getName() {
		return getStr("NAME");
	}

	public void setShortName(java.lang.String shortName) {
		set("SHORT_NAME", shortName);
	}
	
	public java.lang.String getShortName() {
		return getStr("SHORT_NAME");
	}

	public void setRemark(java.lang.String remark) {
		set("REMARK", remark);
	}
	
	public java.lang.String getRemark() {
		return getStr("REMARK");
	}

	public void setCreateDate(java.util.Date createDate) {
		set("CREATE_DATE", createDate);
	}
	
	public java.util.Date getCreateDate() {
		return get("CREATE_DATE");
	}

}
