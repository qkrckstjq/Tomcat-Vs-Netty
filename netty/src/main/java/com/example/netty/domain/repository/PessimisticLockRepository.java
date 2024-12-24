package com.example.netty.domain.repository;

import com.example.netty.domain.entity.PessimisticLock;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.sql.LockMode;
import org.springframework.data.relational.repository.Lock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface PessimisticLockRepository extends ReactiveCrudRepository<PessimisticLock, Long> {

    @Lock(LockMode.PESSIMISTIC_WRITE)
    @Query("SELECT * FROM pessimistic_lock WHERE id = :id FOR UPDATE")
    Mono<PessimisticLock> findByIdWithPessimistic(Long id);
}
