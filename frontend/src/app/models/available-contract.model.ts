/**
 * Defines the requirements for a room, including the number of rooms
 * and the maximum number of adults allowed in each room.
 */
export interface RoomRequirements {
  numberOfRooms: number | null;
  maxAdults: number | null;
}

/**
 * Represents the details of an available room, including the room type
 * and the total price for the room.
 */
export interface AvailableRoomDetail {
  requirementId: number;
  roomType: string;
  totalPrice: number;
}

/**
 * Represents the search criteria for finding available contracts, including
 * check-in date, number of nights, and a list of room requirements.
 */
export interface SearchContract {
  checkInDate: Date | null;
  noOfNights: number | null;
  roomRequirements: RoomRequirements[];
}

/**
 * Represents an available contract, which includes hotel name and a list
 * of available rooms along with their details (type and price).
 */
export interface AvailableContract {
  hotelName: string;
  availableRooms: AvailableRoomDetail[];
}
