package com.helmes.sectorsapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

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
}
