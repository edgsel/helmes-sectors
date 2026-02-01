package com.helmes.sectorsapi.mapper;

import com.helmes.sectorsapi.dto.request.CreateApplicationDTO;
import com.helmes.sectorsapi.dto.response.ApplicationResponseDTO;
import com.helmes.sectorsapi.dto.response.ApplicationSummaryResponseDTO;
import com.helmes.sectorsapi.model.Application;
import com.helmes.sectorsapi.model.Sector;
import com.helmes.sectorsapi.model.User;
import com.helmes.sectorsapi.projection.ApplicationSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {

    public Application toEntity(CreateApplicationDTO applicationCreateDTO, User user, Set<Sector> sectors) {
        return Application.builder()
            .user(user)
            .applicantName(applicationCreateDTO.applicantName())
            .sectors(sectors)
            .agreedToTerms(applicationCreateDTO.agreedToTerms())
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

    public ApplicationSummaryResponseDTO toSummaryResponseDTO(ApplicationSummary summary) {
        return ApplicationSummaryResponseDTO.builder()
            .id(summary.getId())
            .applicantName(summary.getApplicantName())
            .createdAt(summary.getCreatedAt())
            .updatedAt(summary.getUpdatedAt())
            .build();
    }

    public List<ApplicationSummaryResponseDTO> toSummaryResponseDTOList(List<ApplicationSummary> summaries) {
        return summaries.stream()
            .map(this::toSummaryResponseDTO)
            .toList();
    }
}
