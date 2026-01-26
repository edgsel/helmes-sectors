package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.SectorDTO;
import com.helmes.sectorsapi.model.Sector;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    SectorDTO toDto(Sector sector);

    default List<SectorDTO> toTree(List<Sector> sectors) {
        var dtoMap = new HashMap<Integer, SectorDTO>();
        var roots = new ArrayList<SectorDTO>();

        for (Sector sector : sectors) {
            dtoMap.put(sector.getId(), toDto(sector));
        }

        for (Sector sector : sectors) {
            var dto = dtoMap.get(sector.getId());

            if (sector.getParent() != null) {
                var parentDto = dtoMap.get(sector.getParent().getId());
                parentDto.getChildren().add(dto);
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }
}
