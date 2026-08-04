package com.ap_automation.entity;

import com.ap_automation.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "match_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean vendorMatched;

    private boolean poMatched;

    private boolean itemMatched;

    private boolean quantityMatched;

    private boolean priceMatched;

    private boolean totalMatched;

    @Column(length = 2000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

}