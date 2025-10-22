package com.gitauto.drivecare.config;

import com.gitauto.drivecare.database.user_info.entity.UserInfoEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributeAdvice {

    @ModelAttribute
    public void addGlobalAttributes(HttpServletRequest request, Model model) {
        String uri = request.getRequestURI();
        model.addAttribute("currentPath", uri);
    }

    @ModelAttribute("loginUser")
    public UserInfoEntity addLoginUserToModel(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null; // 세션 없으면 null
        return (UserInfoEntity) session.getAttribute("loginUser");
    }
}
