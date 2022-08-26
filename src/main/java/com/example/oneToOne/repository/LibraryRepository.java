package com.example.oneToOne.repository;

import com.example.oneToOne.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Integer>{
}
