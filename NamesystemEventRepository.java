package com.example.mastersapp.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mastersapp.entities.*;


public interface NamesystemEventRepository extends JpaRepository<NamesystemEvent, Long> {
}