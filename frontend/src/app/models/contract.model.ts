//Define a basic RoomDetail model
export interface RoomDetail {
  roomType: string;
  pricePerPerson: number;
  numberOfRooms: number;
  maxAdults: number;
}

//Define a basic contract model
export interface Contract {
  hotelName: string;
  startDate: Date;
  endDate: Date;
  markUpRate: number;
  roomDetails: RoomDetail[];
}
