package com.hexaware.automobileinsurancesystem.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.hexaware.automobileinsurancesystem.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {
 User findByEmail(String email);
//Optional<User> findByEmail(String email);
 User findByAadhaar(String aadhaar);
 User findByPan(String pan);
}

