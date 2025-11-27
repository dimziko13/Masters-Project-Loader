package com.example.mastersapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mastersapp.entities.*;


public interface ResourceRepository extends JpaRepository<Resource, Long> {
    //Optional<Resource> findByPathAndQueryString(String path, String queryString);
	Optional<Resource> findByPath(String path);

}
