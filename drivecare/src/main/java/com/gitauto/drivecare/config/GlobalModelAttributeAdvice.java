package com.gitauto.drivecare.config;

import jakarta.servlet.http.HttpServletRequest;
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
}
