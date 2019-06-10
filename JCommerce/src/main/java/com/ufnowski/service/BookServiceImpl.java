package com.ufnowski.service;

import com.ufnowski.entity.Book;
import com.ufnowski.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        System.out.println(book.getName() + " " + book.getClassName());
        return bookRepository.save(book);
    }

    @Override
    public void save(List<Book> bookList) {
        bookRepository.save(bookList);
    }

    @Override
    public Iterable<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Book findOne(long bookId) {
        return bookRepository.findOne(bookId);
    }

    @Override
    public void delete(long bookId) {
        bookRepository.delete(bookId);
    }

    @Override
    public long count() {
        return bookRepository.count();
    }
}
