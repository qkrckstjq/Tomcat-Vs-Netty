package com.example.netty.domain.repository;

import com.example.netty.domain.entity.JustText;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JustTextRepository extends ReactiveCrudRepository<JustText, Long> {

}
