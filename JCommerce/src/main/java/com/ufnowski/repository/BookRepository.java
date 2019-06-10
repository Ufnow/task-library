package com.ufnowski.repository;

import com.ufnowski.entity.Book;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
}

