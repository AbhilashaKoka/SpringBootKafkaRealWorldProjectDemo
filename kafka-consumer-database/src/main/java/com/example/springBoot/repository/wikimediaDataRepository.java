package com.example.springBoot.repository;
import com.example.springBoot.entity.WikimediaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface wikimediaDataRepository extends JpaRepository<WikimediaData,Long> {
}
