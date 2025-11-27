package com.example.mastersapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mastersapp.entities.*;

public interface HttpMethodRepository extends JpaRepository<HttpMethod, Integer> {
    Optional<HttpMethod> findByMethod(String method);
}
