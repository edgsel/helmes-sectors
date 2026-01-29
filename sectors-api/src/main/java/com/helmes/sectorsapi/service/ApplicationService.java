package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.ApplicationDTO;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.repository.ApplicationRepository;
import com.helmes.sectorsapi.repository.SectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.helmes.sectorsapi.exception.ErrorCode.SECTORS_NOT_FOUND;

@Service
@AllArgsConstructor
public class ApplicationService {

    private final SectorRepository sectorRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public void saveApplication(User user, ApplicationDTO applicationDTO) {
        var sectors = sectorRepository.findAllByIdSet(applicationDTO.getSectorIds());

        validateSectors(applicationDTO.getSectorIds(), sectors);

        applicationRepository.save(Application.toEntity(applicationDTO, user, sectors));
    }

    private static void validateSectors(Set<Long> dtoSectorsIds, Set<Sector> sectorEntities) {
        if (dtoSectorsIds.size() != sectorEntities.size()) {
            var foundIds = sectorEntities.stream()
                .map(Sector::getId)
                .collect(Collectors.toSet());
            var missingIds = new HashSet<>(dtoSectorsIds);

            missingIds.removeAll(foundIds);

            throw new EntityNotFoundException("Sectors not found: " + missingIds, SECTORS_NOT_FOUND.name());
        }
    }
}
