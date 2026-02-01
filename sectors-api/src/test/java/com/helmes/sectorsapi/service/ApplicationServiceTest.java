package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.request.CreateApplicationDTO;
import com.helmes.sectorsapi.dto.request.UpdateApplicationDTO;
import com.helmes.sectorsapi.dto.response.ApplicationResponseDTO;
import com.helmes.sectorsapi.dto.response.ApplicationSummaryResponseDTO;
import com.helmes.sectorsapi.exception.ApplicationNotFoundException;
import com.helmes.sectorsapi.exception.ParentSectorSelectedException;
import com.helmes.sectorsapi.exception.SectorsNotFoundException;
import com.helmes.sectorsapi.mapper.ApplicationMapper;
import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.projection.ApplicationSummary;
import com.helmes.sectorsapi.repository.ApplicationRepository;
import com.helmes.sectorsapi.repository.SectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationService applicationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .id(1L)
            .username("testuser")
            .build();
    }

    @Test
    void fetchApplication_Success() {
        // given
        var applicationId = UUID.randomUUID();
        var application = Application.builder().id(applicationId).build();
        var responseDTO = ApplicationResponseDTO.builder().applicationId(applicationId).build();

        when(applicationRepository.findByIdAndUserId(applicationId, user.getId())).thenReturn(Optional.of(application));
        when(applicationMapper.toResponseDTO(application)).thenReturn(responseDTO);

        // when
        var result = applicationService.fetchApplication(user, applicationId);

        // then
        assertThat(result.applicationId()).isEqualTo(applicationId);
    }

    @Test
    void fetchApplication_NotFound_ThrowsException() {
        // given
        var applicationId = UUID.randomUUID();
        when(applicationRepository.findByIdAndUserId(applicationId, user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> applicationService.fetchApplication(user, applicationId))
            .isInstanceOf(ApplicationNotFoundException.class)
            .hasMessageContaining(applicationId.toString());
    }

    @Test
    void fetchAllApplications_Success() {
        // given
        var summary1 = mock(ApplicationSummary.class);
        var summary2 = mock(ApplicationSummary.class);
        var summaries = List.of(summary1, summary2);
        var dtos = List.of(
            ApplicationSummaryResponseDTO.builder().id(UUID.randomUUID()).build(),
            ApplicationSummaryResponseDTO.builder().id(UUID.randomUUID()).build()
        );

        when(applicationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())).thenReturn(summaries);
        when(applicationMapper.toSummaryResponseDTOList(summaries)).thenReturn(dtos);

        // when
        var result = applicationService.fetchAllApplications(user);

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void fetchAllApplications_Empty_ReturnsEmptyList() {
        // given
        when(applicationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())).thenReturn(List.of());
        when(applicationMapper.toSummaryResponseDTOList(List.of())).thenReturn(List.of());

        // when
        var result = applicationService.fetchAllApplications(user);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void saveApplication_Success() {
        // given
        var sectorIds = Set.of(1L, 2L);
        var createDTO = new CreateApplicationDTO("John Doe", sectorIds, true);
        var application = Application.builder().id(UUID.randomUUID()).build();
        var sectors = Set.of(
            Sector.builder().id(1L).build(),
            Sector.builder().id(2L).build()
        );
        var responseDTO = ApplicationResponseDTO.builder()
            .applicationId(application.getId())
            .applicantName(createDTO.applicantName())
            .build();

        when(sectorRepository.findAllByIdSet(sectorIds)).thenReturn(sectors);
        when(sectorRepository.findParentSectorIds(sectorIds)).thenReturn(Set.of());
        when(applicationMapper.toEntity(createDTO, user, sectors)).thenReturn(application);
        when(applicationRepository.save(application)).thenReturn(application);
        when(applicationMapper.toResponseDTO(application)).thenReturn(responseDTO);

        // when
        var result = applicationService.saveApplication(user, createDTO);

        // then
        assertThat(result.applicantName()).isEqualTo(createDTO.applicantName());
        verify(applicationRepository).save(application);
    }

    @Test
    void saveApplication_SectorNotFound_ThrowsException() {
        // given
        var sectorIds = Set.of(1L, 999L);
        var createDTO = new CreateApplicationDTO("John Doe", sectorIds, true);
        var sectors = Set.of(Sector.builder().id(1L).build()); // only one found

        when(sectorRepository.findAllByIdSet(sectorIds)).thenReturn(sectors);

        // when & then
        assertThatThrownBy(() -> applicationService.saveApplication(user, createDTO))
            .isInstanceOf(SectorsNotFoundException.class)
            .hasMessageContaining("999");

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void saveApplication_ParentSectorSelected_ThrowsException() {
        // given
        var sectorIds = Set.of(1L, 2L);
        var createDTO = new CreateApplicationDTO("John Doe", sectorIds, true);
        var sectors = Set.of(
            Sector.builder().id(1L).build(),
            Sector.builder().id(2L).build()
        );

        when(sectorRepository.findAllByIdSet(sectorIds)).thenReturn(sectors);
        when(sectorRepository.findParentSectorIds(sectorIds)).thenReturn(Set.of(1L)); // sector 1 is a parent

        // when & then
        assertThatThrownBy(() -> applicationService.saveApplication(user, createDTO))
            .isInstanceOf(ParentSectorSelectedException.class)
            .hasMessageContaining("1");

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void updateApplication_Success() {
        // given
        var applicationId = UUID.randomUUID();
        var sectorIds = Set.of(3L, 4L);
        var updateDTO = new UpdateApplicationDTO("Jane Doe", sectorIds);
        var sectors = Set.of(
            Sector.builder().id(3L).build(),
            Sector.builder().id(4L).build()
        );
        var application = Application.builder()
            .id(applicationId)
            .applicantName(updateDTO.applicantName())
            .build();
        var responseDTO = ApplicationResponseDTO.builder()
            .applicationId(applicationId)
            .applicantName(updateDTO.applicantName())
            .build();

        when(applicationRepository.findByIdAndUserId(applicationId, user.getId())) .thenReturn(Optional.of(application));
        when(sectorRepository.findAllByIdSet(sectorIds)).thenReturn(sectors);
        when(sectorRepository.findParentSectorIds(sectorIds)).thenReturn(Set.of());
        when(applicationRepository.save(application)).thenReturn(application);
        when(applicationMapper.toResponseDTO(application)).thenReturn(responseDTO);

        // when
        var result = applicationService.updateApplication(user, applicationId, updateDTO);

        // then
        assertThat(result.applicantName()).isEqualTo(updateDTO.applicantName());
        verify(applicationRepository).save(application);
    }

    @Test
    void updateApplication_NotFound_ThrowsException() {
        // given
        var applicationId = UUID.randomUUID();
        var updateDTO = new UpdateApplicationDTO("Jane Doe", Set.of(1L));

        when(applicationRepository.findByIdAndUserId(applicationId, user.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> applicationService.updateApplication(user, applicationId, updateDTO))
            .isInstanceOf(ApplicationNotFoundException.class);

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void updateApplication_SectorNotFound_ThrowsException() {
        // given
        var applicationId = UUID.randomUUID();
        var sectorIds = Set.of(1L, 999L);
        var updateDTO = new UpdateApplicationDTO("Jane Doe", sectorIds);
        var application = Application.builder().id(applicationId).build();
        var sectors = Set.of(Sector.builder().id(1L).build());

        when(applicationRepository.findByIdAndUserId(applicationId, user.getId())).thenReturn(Optional.of(application));
        when(sectorRepository.findAllByIdSet(sectorIds)).thenReturn(sectors);

        // when & then
        assertThatThrownBy(() -> applicationService.updateApplication(user, applicationId, updateDTO))
            .isInstanceOf(SectorsNotFoundException.class);

        verify(applicationRepository, never()).save(any());
    }
}
