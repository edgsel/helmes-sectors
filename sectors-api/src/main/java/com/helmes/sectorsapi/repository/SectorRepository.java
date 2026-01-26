package com.helmes.sectorsapi.repository;

import com.helmes.sectorsapi.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findAllByOrderBySectorLevelAscIdAsc();
}
