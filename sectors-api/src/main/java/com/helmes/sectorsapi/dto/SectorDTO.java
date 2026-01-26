package com.helmes.sectorsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectorDTO {

    private Integer id;

    private String name;

    private Integer sectorLevel;

    @Builder.Default
    private List<SectorDTO> children = new ArrayList<>();
}
