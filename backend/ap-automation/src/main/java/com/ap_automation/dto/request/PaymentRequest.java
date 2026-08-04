package com.ap_automation.dto.request;

import com.ap_automation.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private String transactionReference;

}