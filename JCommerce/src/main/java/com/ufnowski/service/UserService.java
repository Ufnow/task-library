package com.ufnowski.service;

import com.ufnowski.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    User save(User user);
    void save(Iterable<User> userList);
    Iterable<User> findAll();
    Page<User> findAll(Pageable pageable);
    User findOne(long userId);
    void delete(long userId);
    long count();

}
