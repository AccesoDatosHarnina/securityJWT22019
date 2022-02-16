package com.example.demo.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    protected LocalDateTime timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
	protected String developerMessage;
    //es un mapoa con los datos de respuestas, lo de arriba son metadatos
    protected Map<?, ?> data;
    
    public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Response(LocalDateTime timeStamp, int statusCode, HttpStatus status, String reason, String message,
			String developerMessage, Map<?, ?> data) {
		super();
		this.timeStamp = timeStamp;
		this.statusCode = statusCode;
		this.status = status;
		this.reason = reason;
		this.message = message;
		this.developerMessage = developerMessage;
		this.data = data;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public Map<?, ?> getData() {
		return data;
	}
	public void setData(Map<?, ?> data) {
		this.data = data;
	}

}
