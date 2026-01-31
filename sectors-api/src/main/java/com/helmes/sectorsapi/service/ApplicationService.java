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

@Service
@AllArgsConstructor
public class ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final SectorRepository sectorRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationResponseDTO fetchApplication(User user, UUID applicationId) {
        var application = getApplicationByIdAndUserId(applicationId, user.getId());

        return applicationMapper.toResponseDTO(application);
    }

    public List<ApplicationSummaryResponseDTO> fetchAllApplications(User user) {
        var applications = applicationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        return applicationMapper.toSummaryResponseDTOList(applications);
    }

    @Transactional
    public ApplicationResponseDTO saveApplication(User user, CreateApplicationDTO createApplicationDTO) {
        var sectors = fetchAndValidateSectors(createApplicationDTO.sectorIds());
        var application = applicationRepository.save(applicationMapper.toEntity(createApplicationDTO, user, sectors));

        return applicationMapper.toResponseDTO(application);
    }

    @Transactional
    public ApplicationResponseDTO updateApplication(User user, UUID applicationId, UpdateApplicationDTO updateApplicationDTO) {
        var application = getApplicationByIdAndUserId(applicationId, user.getId());
        var sectors = fetchAndValidateSectors(updateApplicationDTO.sectorIds());

        application.update(updateApplicationDTO.applicantName(), sectors);

        var updated = applicationRepository.save(application);

        return applicationMapper.toResponseDTO(updated);
    }

    private Application getApplicationByIdAndUserId(UUID applicationId, Long userId) {
        return applicationRepository.findByIdAndUserId(applicationId, userId)
            .orElseThrow(() -> new ApplicationNotFoundException("Application %s not found or access denied".formatted(applicationId)));
    }

    private Set<Sector> fetchAndValidateSectors(Set<Long> sectorIds) {
        var sectors = sectorRepository.findAllByIdSet(sectorIds);
        validateSectors(sectorIds, sectors);

        return sectors;
    }

    private void validateSectors(Set<Long> requestedSectorIds, Set<Sector> foundSectors) {
        if (requestedSectorIds.size() != foundSectors.size()) {
            var foundIds = foundSectors.stream()
                .map(Sector::getId)
                .collect(Collectors.toSet());

            var missingIds = new HashSet<>(requestedSectorIds);
            missingIds.removeAll(foundIds);

            throw new SectorsNotFoundException("Sectors not found: " + missingIds);
        }

        if (sectorRepository.anyHasChildren(requestedSectorIds)) {
            throw new ParentSectorSelectedException("Cannot select parent sectors");
        }
    }
}
