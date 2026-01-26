package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.SectorDTO;
import com.helmes.sectorsapi.mapper.SectorMapper;
import com.helmes.sectorsapi.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    public List<SectorDTO> getSectorTree() {
        var sectors = sectorRepository.findAllByOrderBySectorLevelAscIdAsc();
        var dtoList = sectorMapper.toDtoList(sectors);

        return toTree(dtoList);
    }

    private List<SectorDTO> toTree(List<SectorDTO> dtos) {
        var dtoMap = dtos.stream().collect(Collectors.toMap(SectorDTO::getId, Function.identity()));
        var roots = new ArrayList<SectorDTO>();

        for (SectorDTO dto : dtos) {
            if (dto.getParentId() != null) {
                var parentDto = dtoMap.get(dto.getParentId());

                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }
}
