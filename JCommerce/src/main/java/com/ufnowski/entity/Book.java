package com.ufnowski.entity;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Books")
@JsonIdentityInfo(scope = Book.class,generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "Name")
    private String Name;

    @Column(name = "className")
    private String className;

//    @ManyToMany(mappedBy = "books",fetch = FetchType.EAGER)
//    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_books",
        joinColumns = @JoinColumn(name = "books_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "users_id",
                referencedColumnName = "id"))
    private List<User> users;


    public Book(String Name, String className) {
        this.Name = Name;
        this.className = className;
    }

    public Book(String Name, String className, List<User> users) {
        this.Name = Name;
        this.className = className;
        this.users = users;
    }

    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }



}
