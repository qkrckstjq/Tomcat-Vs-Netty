package com.example.tomcat.domain.repository;

import com.example.tomcat.domain.entity.JustText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JustTextRepository extends JpaRepository<JustText, Long> {

}
