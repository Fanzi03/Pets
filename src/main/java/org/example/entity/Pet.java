package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Year;
import org.example.enums.Gender;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
@Builder
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

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public void setAge(int age) {
        this.age = age;
        this.birthYear = Year.now().getValue() - age;
    }

}
