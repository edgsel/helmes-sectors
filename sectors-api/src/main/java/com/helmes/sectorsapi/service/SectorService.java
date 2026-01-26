package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.SectorDTO;
import com.helmes.sectorsapi.mapper.SectorMapper;
import com.helmes.sectorsapi.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    public List<SectorDTO> getSectorTree() {
        var sectors = sectorRepository.findAllByOrderBySectorLevelAscIdAsc();

        return sectorMapper.toTree(sectors);
    }
}
