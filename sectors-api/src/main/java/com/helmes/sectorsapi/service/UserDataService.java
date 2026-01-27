package com.helmes.sectorsapi.service;

import com.helmes.sectorsapi.dto.UserDataDTO;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.UserData;
import com.helmes.sectorsapi.repository.SectorRepository;
import com.helmes.sectorsapi.repository.UserDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.helmes.sectorsapi.exception.ErrorCode.SECTORS_NOT_FOUND;

@Service
@AllArgsConstructor
public class UserDataService {

    private final UserDataRepository userDataRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public void saveUser(UserDataDTO userDataDTO) {
        var sectors = sectorRepository.findAllByIdSet(userDataDTO.getSectorIds());

        validate(userDataDTO.getSectorIds(), sectors);

        userDataRepository.save(UserData.toEntity(userDataDTO.getName(), sectors, userDataDTO.isAgreedToTerms()));
    }

    private static void validate(Set<Long> dtoSectorsIds, Set<Sector> sectorEntities) {
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
