package com.ufnowski.controller;

import com.ufnowski.service.UserService;
import com.ufnowski.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {
    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String showHomepage(Model model) {
        model.addAttribute("booksAmount", bookService.count());
        model.addAttribute("usersAmount", userService.count());
        return "homepage";
    }
}
