package com.young.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果包装类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //响应码，表示此次请求的结果是成功还是失败
    private String code;
    //如果请求失败，msg是描述失败的信息
    private String msg;
    //请求成功的话，在object中存放要返回的数据
    private Object data;

    //无返回数据成功时返回的方法
    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }
    //有返回数据成功时返回的方法
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }
    //无返回数据失败时返回的方法
    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }
    //默认错误方法
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }



}
