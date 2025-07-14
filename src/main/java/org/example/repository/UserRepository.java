package org.example.repository; 
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.userName = :userName")
    Optional<User> findByUserName(@Param("userName") String userName);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.userName = :userName") 
    boolean existsByUserName(@Param("userName") String userName);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

}