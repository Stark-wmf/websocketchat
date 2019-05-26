package com.websocket.entity;

import lombok.Data;
//懒得用了。。。
@Data
public class Response {
    private  Integer code = 0;
    private  String message = "";
    private Object data = null;
    public Response(){
        code = 0;
        message = "请求失败";
        data = null;
    }

    public Response(Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
