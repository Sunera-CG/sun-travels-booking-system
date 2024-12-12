package com.suntravels.callcenter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableContractDTO {

    private String hotelName;
   private List<AvailableRoomDTO> availableRooms;
}
