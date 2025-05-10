package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Year;


@Data
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    @Column(name = "birthyear")
    private int birthYear;

    @Column(name = "created_at")
    private LocalDate createdAt;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id")
//    private User user;

    public void setAge(int age) {
        this.age = age;
        this.birthYear = Year.now().getValue() - age;
    }
}
