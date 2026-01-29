package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.request.ApplicationDTO;
import com.helmes.sectorsapi.dto.response.ApplicationResponseDTO;
import com.helmes.sectorsapi.dto.response.ApplicationSummaryResponseDTO;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.mapper.ApplicationMapper;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.repository.ApplicationRepository;
import com.helmes.sectorsapi.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.helmes.sectorsapi.exception.ErrorCode.APPLICATION_NOT_FOUND;
import static com.helmes.sectorsapi.exception.ErrorCode.SECTORS_NOT_FOUND;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final SectorRepository sectorRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public ApplicationResponseDTO saveApplication(User user, ApplicationDTO applicationDTO) {
        var sectors = sectorRepository.findAllByIdSet(applicationDTO.sectorIds());
        validateSectors(applicationDTO.sectorIds(), sectors);

        var application = applicationRepository.save(applicationMapper.toEntity(applicationDTO, user, sectors));

        return applicationMapper.toResponseDTO(application);
    }

    public ApplicationResponseDTO fetchApplication(User currentUser, UUID applicationId) {
        var application = applicationRepository.findByIdAndUserId(applicationId, currentUser.getId())
            .orElseThrow(() -> new EntityNotFoundException(
                "Application %s not found or access denied".formatted(applicationId),
                APPLICATION_NOT_FOUND.name())
            );

        return applicationMapper.toResponseDTO(application);
    }

    public List<ApplicationSummaryResponseDTO> fetchAllApplications(User currentUser) {
        var applications = applicationRepository.findAllByUserIdOrderByCreatedAtDesc(currentUser.getId());

        return applicationMapper.toApplicationSummaryListDTO(applications);
    }

    private static void validateSectors(Set<Long> requestedSectorIds, Set<Sector> sectorEntities) {
        var foundIds = sectorEntities.stream()
            .map(Sector::getId)
            .collect(Collectors.toSet());

        if (!foundIds.containsAll(requestedSectorIds)) {
            var missingIds = new HashSet<>(requestedSectorIds);
            missingIds.removeAll(foundIds);

            throw new EntityNotFoundException("Sectors not found: " + missingIds, SECTORS_NOT_FOUND.name());
        }
    }
}
