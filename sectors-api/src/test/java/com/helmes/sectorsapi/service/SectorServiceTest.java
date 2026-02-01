package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.response.SectorResponseDTO;
import com.helmes.sectorsapi.mapper.SectorMapper;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.repository.SectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private SectorMapper sectorMapper;

    @InjectMocks
    private SectorService sectorService;

    @Test
    void getSectorTree_Success_ReturnsTreeStructure() {
        // given
        var parentSector = Sector.builder().id(1L).name("Manufacturing").sectorLevel(1).build();
        var childSector = Sector.builder().id(2L).name("Food").parent(parentSector).sectorLevel(2).build();
        var sectors = List.of(parentSector, childSector);

        var parentDto = SectorResponseDTO.builder()
            .id(parentSector.getId())
            .name(parentSector.getName())
            .parentId(null)
            .sectorLevel(parentSector.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var childDto = SectorResponseDTO.builder()
            .id(childSector.getId())
            .name(childSector.getName())
            .parentId(childSector.getParent().getId())
            .sectorLevel(childSector.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dtos = List.of(parentDto, childDto);

        when(sectorRepository.findAllOrderBySectorLevelAndId()).thenReturn(sectors);
        when(sectorMapper.toDtoList(sectors)).thenReturn(dtos);

        // when
        var result = sectorService.getSectorTree();

        // then
        assertThat(result).hasSize(1); // only root
        assertThat(result.get(0).id()).isEqualTo(parentSector.getId());
        assertThat(result.get(0).children()).hasSize(1);
        assertThat(result.get(0).children().get(0).id()).isEqualTo(childSector.getId());
    }

    @Test
    void getSectorTree_MultipleRoots_ReturnsAllRoots() {
        // given
        var root1 = Sector.builder().id(1L).name("Manufacturing").sectorLevel(1).build();
        var root2 = Sector.builder().id(2L).name("Service").sectorLevel(1).build();
        var sectors = List.of(root1, root2);

        var dto1 = SectorResponseDTO.builder()
            .id(1L)
            .name(root1.getName())
            .parentId(null)
            .sectorLevel(root1.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dto2 = SectorResponseDTO.builder()
            .id(root2.getId())
            .name(root2.getName())
            .parentId(null)
            .sectorLevel(root2.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dtos = List.of(dto1, dto2);

        when(sectorRepository.findAllOrderBySectorLevelAndId()).thenReturn(sectors);
        when(sectorMapper.toDtoList(sectors)).thenReturn(dtos);

        // when
        var result = sectorService.getSectorTree();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void getSectorTree_DeepNesting_BuildsCorrectHierarchy() {
        // given
        var level1 = Sector.builder().id(1L).name("Manufacturing").sectorLevel(1).build();
        var level2 = Sector.builder().id(2L).name("Food").parent(level1).sectorLevel(2).build();
        var level3 = Sector.builder().id(3L).name("Bakery").parent(level2).sectorLevel(3).build();
        var sectors = List.of(level1, level2, level3);

        var dto1 = SectorResponseDTO.builder()
            .id(1L)
            .name(level1.getName())
            .parentId(null)
            .sectorLevel(level1.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dto2 = SectorResponseDTO.builder()
            .id(level2.getId())
            .name(level2.getName())
            .parentId(level2.getParent().getId())
            .sectorLevel(level2.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dto3 = SectorResponseDTO.builder()
            .id(level3.getId())
            .name(level3.getName())
            .parentId(level3.getParent().getId())
            .sectorLevel(level3.getSectorLevel())
            .children(new ArrayList<>())
            .build();
        var dtos = List.of(dto1, dto2, dto3);

        when(sectorRepository.findAllOrderBySectorLevelAndId()).thenReturn(sectors);
        when(sectorMapper.toDtoList(sectors)).thenReturn(dtos);

        // when
        var result = sectorService.getSectorTree();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).children()).hasSize(1);
        assertThat(result.get(0).children().get(0).children()).hasSize(1);
        assertThat(result.get(0).children().get(0).children().get(0).name()).isEqualTo(level3.getName());
    }

    @Test
    void getSectorTree_Empty_ReturnsEmptyList() {
        // given
        when(sectorRepository.findAllOrderBySectorLevelAndId()).thenReturn(List.of());
        when(sectorMapper.toDtoList(List.of())).thenReturn(List.of());

        // when
        var result = sectorService.getSectorTree();

        // then
        assertThat(result).isEmpty();
    }
}