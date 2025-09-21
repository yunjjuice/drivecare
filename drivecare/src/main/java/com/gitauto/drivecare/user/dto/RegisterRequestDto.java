package com.gitauto.drivecare.user.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String userId;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
    private String telNo;
    private String auth;
}
