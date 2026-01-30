package com.helmes.sectorsapi.projection;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ApplicationSummary {
    UUID getId();
    OffsetDateTime getCreatedAt();
    OffsetDateTime getUpdatedAt();
}
