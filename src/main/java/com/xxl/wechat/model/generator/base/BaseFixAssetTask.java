package com.xxl.wechat.model.generator.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseFixAssetTask<M extends BaseFixAssetTask<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Integer id) {
		set("ID", id);
	}
	
	public java.lang.Integer getId() {
		return getInt("ID");
	}

	public void setFixUserId(java.lang.String fixUserId) {
		set("FIX_USER_ID", fixUserId);
	}
	
	public java.lang.String getFixUserId() {
		return getStr("FIX_USER_ID");
	}

	public void setApplyUserId(java.lang.String applyUserId) {
		set("APPLY_USER_ID", applyUserId);
	}
	
	public java.lang.String getApplyUserId() {
		return getStr("APPLY_USER_ID");
	}

	public void setAssetType(java.lang.String assetType) {
		set("ASSET_TYPE", assetType);
	}
	
	public java.lang.String getAssetType() {
		return getStr("ASSET_TYPE");
	}

	public void setAssetSubType(java.lang.String assetSubType) {
		set("ASSET_SUB_TYPE", assetSubType);
	}
	
	public java.lang.String getAssetSubType() {
		return getStr("ASSET_SUB_TYPE");
	}

	public void setAssetName(java.lang.String assetName) {
		set("ASSET_NAME", assetName);
	}
	
	public java.lang.String getAssetName() {
		return getStr("ASSET_NAME");
	}

	public void setAssetLocation(java.lang.String assetLocation) {
		set("ASSET_LOCATION", assetLocation);
	}
	
	public java.lang.String getAssetLocation() {
		return getStr("ASSET_LOCATION");
	}

	public void setFixReason(java.lang.String fixReason) {
		set("FIX_REASON", fixReason);
	}
	
	public java.lang.String getFixReason() {
		return getStr("FIX_REASON");
	}

	public void setStatus(java.lang.Integer status) {
		set("STATUS", status);
	}
	
	public java.lang.Integer getStatus() {
		return getInt("STATUS");
	}

	public void setApplyDate(java.util.Date applyDate) {
		set("APPLY_DATE", applyDate);
	}
	
	public java.util.Date getApplyDate() {
		return get("APPLY_DATE");
	}

	public void setStartFixDate(java.util.Date startFixDate) {
		set("START_FIX_DATE", startFixDate);
	}
	
	public java.util.Date getStartFixDate() {
		return get("START_FIX_DATE");
	}

	public void setFixedDate(java.util.Date fixedDate) {
		set("FIXED_DATE", fixedDate);
	}
	
	public java.util.Date getFixedDate() {
		return get("FIXED_DATE");
	}

	public void setVersion(java.lang.Integer version) {
		set("VERSION", version);
	}
	
	public java.lang.Integer getVersion() {
		return getInt("VERSION");
	}

}
