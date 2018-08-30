package com.xxl.wechat.entity;


/**
 * 所有返回到页面的对象容器
 * @author wangpeng
 *
 */
public class ResponseResult<T> {
	
	private boolean success;
	
    private String message;
    
    private T data; 
    
    /* 不提供直接设置errorCode的接口，只能通过setErrorInfo方法设置错误信息 */
    private String errorCode;
    
    private ResponseResult() {
    	
    }
	
	public static ResponseResult instance(){
		return new ResponseResult();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public ResponseResult<T> setInstance(boolean success,T data){
    	this.success = success;
    	this.data = data;
    	return this;
	}

	public ResponseResult<T> setErrorMsg(boolean flag ,String errorMsg){
		this.success = flag;
		this.data = null;
		this.message = errorMsg;
		return this;
	}

}
