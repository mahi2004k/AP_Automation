package com.ap_automation.dto.response;

import com.ap_automation.enums.PaymentMethod;
import com.ap_automation.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private String paymentNumber;

    private Long invoiceId;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private PaymentMethod paymentMethod;

    private String transactionReference;

    private PaymentStatus status;

}