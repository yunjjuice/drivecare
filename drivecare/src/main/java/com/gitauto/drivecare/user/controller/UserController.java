package com.gitauto.drivecare.user.controller;

import com.gitauto.drivecare.user.dto.LoginRequestDto;
import com.gitauto.drivecare.user.dto.RegisterRequestDto;
import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import com.gitauto.drivecare.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                return "redirect:/owner/dashboard";
            } else {
                model.addAttribute("errorMessage", "알 수 없는 권한입니다.");
                return "user/login";
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 무효화 (모든 속성 제거)
        session.invalidate();
        // 로그아웃 후 로그인 페이지로 리다이렉트
        return "redirect:/login";
    }

    // 회원가입
    @GetMapping("/register")
    public String viewRegisterPage(RegisterRequestDto form,
                                   Model model,
                                   @RequestParam(value = "success", required = false) String success) {
        if (success != null) model.addAttribute("successMessage", "회원가입이 완료되었습니다.");
        return "user/register";
    }

    @GetMapping("/check-id")
    @ResponseBody
    public Map<String, Object> checkUserId(@RequestParam String userId) {
        boolean exists = userService.checkUserIdDuplicate(userId);
        return Map.of("exists", exists);
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequestDto registerRequestDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes ra) {

        // 비밀번호 일치 검증(컨트롤러에서 필드 에러로 표시)
        if (!registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "Mismatch", "비밀번호와 확인이 일치하지 않습니다.");
        }

        // 아이디 중복 에러는 서비스에서 예외를 던지지 말고 컨트롤러에서 깔끔히 처리
        if (userService.checkUserIdDuplicate(registerRequestDto.getUserId())) {
            bindingResult.rejectValue("userId", "Duplicate", "이미 사용 중인 아이디입니다.");
        }

        if (userService.checkEmailDuplicate(registerRequestDto.getEmail())) {
            bindingResult.rejectValue("email", "Duplicate", "이미 사용 중인 이메일입니다.");
        }

        // 권한이 사용자일 때 차량정보 필수
        if ("USER".equalsIgnoreCase(registerRequestDto.getAuth())) {
            if (registerRequestDto.getCarNumber() == null || registerRequestDto.getCarNumber().isBlank()) {
                bindingResult.rejectValue("carNumber", "Required", "차량번호를 입력하세요.");
            }
            if (registerRequestDto.getMaker() == null || registerRequestDto.getMaker().isBlank()) {
                bindingResult.rejectValue("maker", "Required", "자동차 브랜드를 선택하세요.");
            }
            if (registerRequestDto.getModel() == null || registerRequestDto.getModel().isBlank()) {
                bindingResult.rejectValue("model", "Required", "자동차 모델을 입력하세요.");
            }
            if (registerRequestDto.getYear() == null || registerRequestDto.getYear().isBlank()) {
                bindingResult.rejectValue("year", "Required", "자동차 연식을 선택하세요.");
            }
        }

        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        try {
            userService.register(registerRequestDto);
            // 플래시 메시지 or 쿼리 파라미터로 성공 안내
            ra.addFlashAttribute("successMessage", "회원가입이 완료되었습니다.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            // 서비스단에서 혹시나 던진 예외는 글로벌 에러로 표시
            model.addAttribute("errorMessage", e.getMessage());
            return "user/register";
        }
    }
}
