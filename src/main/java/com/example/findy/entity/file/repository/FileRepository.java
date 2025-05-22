package com.example.findy.entity.file.repository;

import com.example.findy.entity.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
