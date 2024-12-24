package com.example.tomcat.domain.repository;

import com.example.tomcat.domain.entity.PessimisticLock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessimisticLockRepository extends JpaRepository<PessimisticLock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pl FROM PessimisticLock pl WHERE pl.id = :id")
    Optional<PessimisticLock> findByIdWithPessimistic(Long id);
}
