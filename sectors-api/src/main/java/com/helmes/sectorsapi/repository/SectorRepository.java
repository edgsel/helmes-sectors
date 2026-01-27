package com.helmes.sectorsapi.repository;

import com.helmes.sectorsapi.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findAllByOrderBySectorLevelAscIdAsc();

    @Query("SELECT s FROM Sector s WHERE s.id IN :ids")
    Set<Sector> findAllByIdSet(@Param("ids") Set<Long> ids);
}
