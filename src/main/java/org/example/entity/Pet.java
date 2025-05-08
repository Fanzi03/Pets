package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Year;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
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

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDate createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setAge(int age) {
        this.age = age;
        this.birthYear = Year.now().getValue() - age;
    }

}
