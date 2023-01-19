package com.young.dto;

import lombok.Data;

/**
 * 用于接收前端登录请求的参数
 */
@Data
public class UserDTO {
    private String username;
    private String password;
}
