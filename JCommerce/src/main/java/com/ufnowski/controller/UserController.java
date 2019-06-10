package com.ufnowski.controller;

import com.ufnowski.entity.User;
import com.ufnowski.service.UserService;
import com.ufnowski.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class UserController {
    public final int PAGE_SIZE = 3;
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;



    @GetMapping(value = {"/add-user", "/add-user/"})
    public String addUserPage(Model model) {
        model.addAttribute("usersAmount", userService.count());
        model.addAttribute("user", new User());
        return "addUser";
    }

    @GetMapping(value = {"/modify-user", "/modify-user/", "/modify-user/{pageNumber}"})
    public String modifyUserPage(@PathVariable Optional<Integer> pageNumber, Model model) {
        if (!pageNumber.isPresent() || pageNumber.get()<0) pageNumber = Optional.of(0);

        Page<User> userList = getUserPage(pageNumber.get());

        model.addAttribute("userList", userList);
        model.addAttribute("currentPage", userList.getNumber());
        model.addAttribute("totalPages", userList.getTotalPages());
        return "modifyUser";
    }

    private Page<User> getUserPage(int pageNumber) {
        Page<User> userPage;
        PageRequest userPageRequest = new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "userName");
        userPage = userService.findAll(userPageRequest);
        if (pageNumber > userPage.getTotalPages()) {
            userPage = getUserPage(userPage.getTotalPages()-1);
        }
        return userPage;
    }

    @GetMapping("/modify-user/modify/{userId}")
    public String modifyUser(Model model, @PathVariable long userId) {
        User user = userService.findOne(userId);
        model.addAttribute("user", user);
        model.addAttribute("usersAmount", userService.count());
        return "modifyUserPage";
    }
}
