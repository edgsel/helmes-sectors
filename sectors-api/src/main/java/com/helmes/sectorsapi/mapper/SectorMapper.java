package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.SectorDTO;
import com.helmes.sectorsapi.model.Sector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    @Mapping(target = "children", ignore = true)
    @Mapping(source = "parent.id", target = "parentId")
    SectorDTO toDto(Sector sector);

    List<SectorDTO> toDtoList(List<Sector> sectors);
}
