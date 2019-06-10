package com.ufnowski.controller;

import com.ufnowski.entity.Book;
import com.ufnowski.entity.User;
import com.ufnowski.service.UserService;
import com.ufnowski.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class ControllerRest {

    @Autowired
    BookService bookService;

    @Autowired
    UserService userService;

    @GetMapping("/books")
    public ResponseEntity<Iterable<Book>> bookList() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Book> showBook(@PathVariable long bookId) {
        Book book = bookService.findOne(bookId);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/books/{bookId}/users")
    public ResponseEntity<Iterable<User>> bookUsers(@PathVariable long bookId) {
        Book book = bookService.findOne(bookId);
        if (book != null) {
            return new ResponseEntity<>(book.getUsers(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/books", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {
        System.out.println(book.toString());
        bookService.save(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("api/books/{bookId}").buildAndExpand(book.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/delete/{bookId}")
    public ResponseEntity<Book> deleteBook(@PathVariable long bookId){
        Book book = bookService.findOne(bookId);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            book.getUsers().clear();
            bookService.delete(bookId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/books/{bookId}/remove/{userId}")
    public ResponseEntity<Void> deleteUserFromBook(@PathVariable long bookId, @PathVariable long userId) {
        return deleteBookFromUser(bookId, userId);
    }

    @PutMapping(value = "/books/update/{bookId}")
    public ResponseEntity<Void> updateBook(@PathVariable long bookId, @RequestBody Book book) {
        Book bookToChange = bookService.findOne(bookId);
        if ( book != null && bookToChange != null) {
            bookService.save(book);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }












    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> userList() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> showUser(@PathVariable long userId) {
        User user = userService.findOne(userId);
        if ( user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users/{userId}/books")
    public ResponseEntity<Iterable<Book>> userBooks(@PathVariable long userId) {
        User user = userService.findOne(userId);
        if ( user != null) {
            return new ResponseEntity<>(user.getBooks(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/users" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        userService.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("api/users/{userId}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId){
        User user = userService.findOne(userId);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            for (Book book : user.getBooks()) {
                book.getUsers().remove(user);
                bookService.save(book);
            }
            userService.delete(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/users/{userId}/remove/{bookId}")
    public ResponseEntity<Void> deleteBookFromUser(@PathVariable long bookId, @PathVariable long userId) {
        Book book = bookService.findOne(bookId);
        User user = userService.findOne(userId);
        if(book != null && user != null) {
            book.getUsers().remove(user);
            bookService.save(book);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(value = "/users/update/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable long userId, @RequestBody User user) {
        User userToChange = userService.findOne(userId);
        if ( user != null && userToChange != null) {
            System.out.println("userToChange: " + userToChange.toString());
            System.out.println("user: " + user.toString());
            userService.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
