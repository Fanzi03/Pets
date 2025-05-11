package org.example.repository;

import org.example.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Modifying
    @Query("UPDATE Pet p SET p.age =  p.age +1")
    public void incrementAllAge();
}
