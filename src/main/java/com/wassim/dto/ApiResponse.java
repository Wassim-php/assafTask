package com.wassim.dto;

public class ApiResponse<T> {
    private String message;
    private T data;
    private boolean state;

    public ApiResponse(String message, T data, boolean state){
        this.message = message;
        this.data = data;
        this.state = state;
    }

    public String getMessage(){
        return this.message;
    }
    public T getData(){
        return this.data;
    }
    public boolean getState(){
        return this.state;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setData(T data){
        this.data = data;
    }

    public void setState(boolean state){
        this.state = state;
    }

}
