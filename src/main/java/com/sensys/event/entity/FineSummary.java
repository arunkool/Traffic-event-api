package com.sensys.event.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FineSummary {
    private Double paidFine;
    private Double ToBePaidFine;
}
