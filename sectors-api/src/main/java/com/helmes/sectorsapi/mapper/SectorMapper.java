package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.SectorResponseDTO;
import com.helmes.sectorsapi.model.Sector;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectorMapper {

    public SectorResponseDTO toDto(Sector sector) {
        if (sector == null) {
            return null;
        }

        return SectorResponseDTO.builder()
            .id(sector.getId())
            .name(sector.getName())
            .parentId(sector.getParent() != null ? sector.getParent().getId() : null)
            .sectorLevel(sector.getSectorLevel())
            .build();
    }

    public List<SectorResponseDTO> toDtoList(List<Sector> sectors) {
        if (sectors == null || sectors.isEmpty()) {
            return List.of();
        }

        return sectors.stream()
            .map(this::toDto)
            .toList();
    }
}
