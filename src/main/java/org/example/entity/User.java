package org.example.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", unique = true)
    private String fullName;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private int age;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Pet> pets;
}
