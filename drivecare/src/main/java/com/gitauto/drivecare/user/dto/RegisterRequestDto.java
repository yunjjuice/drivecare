package com.gitauto.drivecare.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력하세요.")
    private String confirmPassword;
    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "전화번호를 입력하세요.")
    @Pattern(regexp = "^(01[0-9]-?[0-9]{3,4}-?[0-9]{4})$|^([0-9]{10,11})$",
            message = "전화번호 형식이 올바르지 않습니다.")
    private String telNo;
    @NotBlank(message = "권한을 선택하세요.")
    private String auth;

    // --- 차량정보: auth=user 일 때만 요구 ---
    private String carNumber;
    private String maker;
    private String model;
    private String year;
}
