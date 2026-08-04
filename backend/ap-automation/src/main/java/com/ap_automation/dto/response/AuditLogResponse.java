package com.ap_automation.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {

    private Long id;

    private String action;

    private String entityType;

    private Long entityId;

    private String username;

    private String details;

    private LocalDateTime createdAt;

}