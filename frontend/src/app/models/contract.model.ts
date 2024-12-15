/**
 * Represents the details of a room offered in a contract, including the room type,
 * price per person, number of rooms available, and maximum number of adults allowed.
 */
export interface RoomDetail {
  roomType: string;
  pricePerPerson: number | null;
  numberOfRooms: number | null;
  maxAdults: number | null;
}

/**
 * Represents a contract with a hotel, including the contract ID, hotel name,
 * start and end dates, markup rate, and a list of room details available under this contract.
 */
export interface Contract {
  contractId: number | null;
  hotelName: string;
  startDate: Date | null;
  endDate: Date | null;
  markUpRate: number | null;
  roomDetails: RoomDetail[];
}
