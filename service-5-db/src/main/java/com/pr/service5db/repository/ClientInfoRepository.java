package com.pr.service5db.repository;

import com.pr.service5db.model.ClientInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfoEntity, UUID> {
    Optional<ClientInfoEntity> findByClientId(UUID uuid);
}
