package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.response.SectorResponseDTO;
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

    public List<SectorResponseDTO> getSectorTree() {
        var sectors = sectorRepository.findAllOrderBySectorLevelAndId();
        var dtoList = sectorMapper.toDtoList(sectors);

        return toTree(dtoList);
    }

    private List<SectorResponseDTO> toTree(List<SectorResponseDTO> dtos) {
        var roots = new ArrayList<SectorResponseDTO>();
        var dtoMap = dtos.stream().collect(Collectors.toMap(SectorResponseDTO::id, Function.identity()));

        for (SectorResponseDTO dto : dtos) {
            if (dto.parentId() != null) {
                var parentDto = dtoMap.get(dto.parentId());

                if (parentDto != null) {
                    parentDto.children().add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }
}
