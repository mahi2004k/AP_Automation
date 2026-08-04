package com.ap_automation.dto.response;

import com.ap_automation.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private Long id;

    private String fullName;

    private String email;

    private Role role;
}
