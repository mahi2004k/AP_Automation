package com.ap_automation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderRequest {

    @NotBlank
    private String poNumber;

    @NotBlank
    private String vendorName;

    @Valid
    private List<PurchaseOrderItemRequest> items;

}