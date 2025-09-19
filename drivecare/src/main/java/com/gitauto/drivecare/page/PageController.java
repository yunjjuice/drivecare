package com.gitauto.drivecare.page;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/page")
@AllArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("pageDto", new PageDto());

        List<PageEntity> users = pageService.findAll();
        model.addAttribute("users", users);

        model.addAttribute("today", LocalDate.now());
        return "test";
    }

    @PostMapping("/hello")
    public String hello(@ModelAttribute PageDto pageDto) {
        pageService.save(pageDto);

        return "redirect:/page/hello";
    }
}
