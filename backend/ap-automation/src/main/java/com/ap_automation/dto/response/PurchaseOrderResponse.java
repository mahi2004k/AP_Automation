package com.ap_automation.dto.response;

import com.ap_automation.enums.PurchaseOrderStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponse {

    private Long id;

    private String poNumber;

    private String vendorName;

    private PurchaseOrderStatus status;

    private List<PurchaseOrderItemResponse> items;

}