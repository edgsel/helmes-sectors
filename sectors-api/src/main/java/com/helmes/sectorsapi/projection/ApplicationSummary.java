package com.helmes.sectorsapi.projection;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ApplicationSummary {
    UUID getId();
    String getApplicantName();
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
}
