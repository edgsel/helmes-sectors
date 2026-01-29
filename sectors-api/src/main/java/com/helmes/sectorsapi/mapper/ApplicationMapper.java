package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.ApplicationDTO;
import com.helmes.sectorsapi.dto.ApplicationResponseDTO;
import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ApplicationMapper {

    public Application toEntity(ApplicationDTO applicationDTO, User user, Set<Sector> sectors) {
        return Application.builder()
            .user(user)
            .applicantName(applicationDTO.applicantName())
            .sectors(sectors)
            .agreedToTerms(applicationDTO.agreedToTerms())
            .build();
    }

    public ApplicationResponseDTO toResponseDTO(Application application, Set<Long> sectorIds) {
        return ApplicationResponseDTO.builder()
            .applicationId(application.getId())
            .applicantName(application.getApplicantName())
            .sectorIds(sectorIds)
            .agreedToTerms(application.getAgreedToTerms())
            .build();
    }
}
