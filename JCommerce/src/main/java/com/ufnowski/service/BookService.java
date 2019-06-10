package com.ufnowski.service;

import com.ufnowski.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;


public interface BookService {
    Book save(Book book);
    void save(List<Book> bookList);
    Iterable<Book> findAll();
    Page<Book> findAll(Pageable pageable);
    Book findOne(long bookId);
    void delete(long bookId);
    long count();
}
