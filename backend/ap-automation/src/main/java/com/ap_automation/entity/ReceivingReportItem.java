package com.ap_automation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "receiving_report_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivingReportItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal quantityReceived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_report_id")
    private ReceivingReport receivingReport;

}