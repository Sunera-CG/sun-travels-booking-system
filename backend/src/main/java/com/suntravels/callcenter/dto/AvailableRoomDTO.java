package com.suntravels.callcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableRoomDTO {

    private Integer requirementId;
    private String roomType;
    private Double markUpPrice;
}
