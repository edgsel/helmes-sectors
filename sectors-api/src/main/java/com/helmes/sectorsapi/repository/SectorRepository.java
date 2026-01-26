package com.helmes.sectorsapi.repository;

import com.helmes.sectorsapi.model.Sector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SectorRepository extends CrudRepository<Sector, Long> {
}

