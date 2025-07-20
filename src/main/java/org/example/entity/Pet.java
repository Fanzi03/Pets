package org.example.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.Year;

import org.example.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String type;
    @Enumerated(EnumType.STRING)
    Gender gender;
    int age;
    @Column(name = "birthyear")
    int birthYear;

    @Column(name = "created_at")
    LocalDate createdAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    User user;

    public void setAge(int age) {
        this.age = age;
        this.birthYear = Year.now().getValue() - age;
    }
}