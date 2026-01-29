package com.helmes.sectorsapi.repository;

import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.projection.ApplicationSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    List<Application> findAllByUserId(Long userId);

    Optional<Application> findByIdAndUserId(UUID id, Long userId);

    List<ApplicationSummary> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
