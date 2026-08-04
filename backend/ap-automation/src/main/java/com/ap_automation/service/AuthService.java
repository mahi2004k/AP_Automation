package com.ap_automation.service;

import com.ap_automation.dto.request.LoginRequest;
import com.ap_automation.dto.request.RegisterRequest;
import com.ap_automation.dto.response.LoginResponse;
import com.ap_automation.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
