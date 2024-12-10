package com.suntravels.callcenter.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableContract {

    private String hotelName;
    private String roomType;
    private Double markUpPrice;
    private String status;
}
