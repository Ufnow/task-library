package com.ufnowski.controller;

import com.ufnowski.entity.Book;
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
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private final int PAGE_SIZE = 3;

    @GetMapping(value = {"/add-book", "/add-book/"})
    public String addBookPage(Model model) {
        model.addAttribute("booksAmount", bookService.count());
        model.addAttribute("book", new Book());
        model.addAttribute("userList", userService.findAll());
        return "addBook";
    }

    @GetMapping(value = {"/modify-book", "/modify-book/", "/modify-book/{pageNumber}"})
    public String modifyBookPage(@PathVariable Optional<Integer> pageNumber, Model model) {
        if (!pageNumber.isPresent() || pageNumber.get()<0) pageNumber = Optional.of(0);

        Page<Book> bookPage = getBookPage(pageNumber.get());

        model.addAttribute("bookList", bookPage);
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        return "modifyBook";
    }

    private Page<Book> getBookPage(int pageNumber) {
        Page<Book> booksPage;
        PageRequest bookPageRequest = new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "className");
        booksPage = bookService.findAll(bookPageRequest);
        if (pageNumber > booksPage.getTotalPages()) {
            booksPage = getBookPage(booksPage.getTotalPages()-1);
        }
        return booksPage;
    }

    @GetMapping("/modify-book/modify/{bookId}")
    public String modifyBook(Model model, @PathVariable long bookId) {
        Book book = bookService.findOne(bookId);
        model.addAttribute("book", book);
        model.addAttribute("userList", userService.findAll());
        return "modifyBookPage";
    }
}
