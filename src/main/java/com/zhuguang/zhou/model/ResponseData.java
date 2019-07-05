package com.zhuguang.zhou.model;


import java.lang.reflect.ParameterizedType;

/**
 * @author zhoushisheng
 * 响应结果
 * @param <T>
 */

public class ResponseData<T> {
	/** 错误或者成功代码 */
	private int code;
	/** 错误描述 */
	private String msg;
	/** 响应结果*/
	private T data;
	private String traceId;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * @param traceId the traceId to set
	 */
	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}
	/**
	 * @return the traceId
	 */
	public String getTraceId() {
		return traceId;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public ResponseData setData(T data) {
		this.data = data;
		return this;
	}

	public void responseCode(int code, String message) {
        this.code = code;
        this.msg = message;
    }

	public Class getDataType(){
		Class<?> clazz = this.getData().getClass();
		return clazz;
	}
}
