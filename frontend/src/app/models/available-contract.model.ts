//Define a basic AvailableRoomDetail model
export interface RoomRequirements {
  numberOfRooms: number | null;
  maxAdults: number | null;
}

//Define a basic RoomRequirements model
export interface AvailableRoomDetail {
  requirementId: number;
  roomType: string;
  totalPrice: number;
}

//Define a basic SearchContract model
export interface SearchContract {
  checkInDate: Date | null;
  noOfNights: number | null;
  roomRequirements: RoomRequirements[];
}

//Define a basic AvailableContract model
export interface AvailableContract {
  hotelName: string;
  availableRooms: AvailableRoomDetail[];
}
