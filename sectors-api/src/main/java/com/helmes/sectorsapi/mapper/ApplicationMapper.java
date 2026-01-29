package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.ApplicationDTO;
import com.helmes.sectorsapi.dto.ApplicationResponseDTO;
import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

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

    public ApplicationResponseDTO toResponseDTO(Application application) {
        return ApplicationResponseDTO.builder()
            .applicationId(application.getId())
            .applicantName(application.getApplicantName())
            .sectorIds(application.getSectors().stream().map(Sector::getId).collect(Collectors.toSet()))
            .agreedToTerms(application.getAgreedToTerms())
            .build();
    }
}
