package com.helmes.sectorsapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SectorResponseDTO(
    Long id,
    String name,
    Long parentId,
    Integer sectorLevel,
    List<SectorResponseDTO> children
) {
    public SectorResponseDTO {
        if (children == null) {
            children = new ArrayList<>();
        }
    }
}
