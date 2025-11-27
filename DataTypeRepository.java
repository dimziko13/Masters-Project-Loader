package com.example.mastersapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mastersapp.entities.*;



public interface DataTypeRepository extends JpaRepository<DataType, Integer> {
    Optional<DataType> findByTypeName(String name);
}