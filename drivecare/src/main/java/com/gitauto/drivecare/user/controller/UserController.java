package com.gitauto.drivecare.user.controller;

import com.gitauto.drivecare.user.dto.LoginRequestDto;
import com.gitauto.drivecare.user.dto.RegisterRequestDto;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인
    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequestDto loginRequestDto, HttpSession session, Model model) {
        try {
            UserInfoEntity user = userService.login(loginRequestDto);
            session.setAttribute("loginUser", user);
            session.setMaxInactiveInterval(60 * 60 * 6);
            if ("dealer".equals(user.getAuth()) || "admin".equals(user.getAuth())) {
                return "redirect:/dealer/dashboard";
            } else if ("user".equals(user.getAuth())) {
                return "redirect:/main/user";
            } else {
                model.addAttribute("errorMessage", "알 수 없는 권한입니다.");
                return "user/login";
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/login";
        }
    }

    // 회원가입
    @GetMapping("/register")
    public String viewRegisterPage(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDto());
        return "user/register";
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkUserId(@RequestParam String userId) {
        boolean exists = userService.checkUserIdDuplicate(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("exists", exists);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequestDto registerRequestDto, Model model) {
        try {
            userService.register(registerRequestDto);
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/register";
        }
    }
}
