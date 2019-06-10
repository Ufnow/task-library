package com.ufnowski.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
@JsonIdentityInfo(scope = User.class,generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "userName")
    private String userName;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "books_users",
//            joinColumns = @JoinColumn(name = "books_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "users_id",
//                    referencedColumnName = "id"))
//    @JsonBackReference
    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Book> books;

    public User(String userName) {
        this.userName = userName;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", books=" + books +
                '}';
    }
}
