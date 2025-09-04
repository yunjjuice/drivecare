package com.gitauto.drivecare.page;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/page")
public class PageController {
    @GetMapping("/hello")
    public String hello(Model model, HttpSession session) {
        model.addAttribute("pageDto", new PageDto());

        List<String> users = (List<String>) session.getAttribute("users");
        if (users == null) users = new ArrayList<>();
        model.addAttribute("users", users);

        model.addAttribute("today", LocalDate.now());
        return "test";
    }

    @PostMapping("/hello")
    public String hello(@ModelAttribute PageDto pageDto, Model model, HttpSession session) {
        List<String> users = (List<String>) session.getAttribute("users");
        if (users == null) users = new ArrayList<>();

        users.add(pageDto.getName());

        session.setAttribute("users", users);

        model.addAttribute("users", users);
        model.addAttribute("today", LocalDate.now());

        model.addAttribute("pageDto", PageDto.builder().build());

        return "test";
    }
}
