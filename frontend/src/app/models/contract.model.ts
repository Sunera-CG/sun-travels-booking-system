//Define a basic RoomDetail model
export interface RoomDetail {
  roomType: string;
  pricePerPerson: number | null;
  numberOfRooms: number | null;
  maxAdults: number | null;
}

//Define a basic contract model
export interface Contract {
  contractId: number | null;
  hotelName: string;
  startDate: Date | null;
  endDate: Date | null;
  markUpRate: number | null;
  roomDetails: RoomDetail[];
}
