import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

//Define a basic contract model
export interface Contract {
  contractId: number;
  hotelName: string;
  startDate: Date;
  endDate: Date;
  markUp: number;
  roomDetails: {
    roomType: string;
    pricePerPerson: number;
    numberOfRooms: number;
    maxAdults: number;
  }[];
}

@Injectable({
  providedIn: 'root',
})
export class ContractService {
  // Mock data for contracts
  private contracts: Contract[] = [
    {
      contractId: 1,
      hotelName: 'Hotel Sunshine',
      startDate: new Date('2024-01-01'),
      endDate: new Date('2024-01-07'),
      markUp: 0.15,
      roomDetails: [
        {
          roomType: 'Single',
          pricePerPerson: 100,
          numberOfRooms: 2,
          maxAdults: 2,
        },
        {
          roomType: 'Double',
          pricePerPerson: 150,
          numberOfRooms: 3,
          maxAdults: 4,
        },
        {
          roomType: 'Single',
          pricePerPerson: 100,
          numberOfRooms: 3,
          maxAdults: 4,
        },
      ],
    },
    {
      contractId: 2,
      hotelName: 'Beachside Resort',
      startDate: new Date('2024-02-01'),
      endDate: new Date('2024-02-10'),
      markUp: 0.15,
      roomDetails: [
        {
          roomType: 'Suite',
          pricePerPerson: 200,
          numberOfRooms: 1,
          maxAdults: 2,
        },
        {
          roomType: 'Family Room',
          pricePerPerson: 180,
          numberOfRooms: 2,
          maxAdults: 4,
        },
      ],
    },
  ];

  constructor() {}

  //simulate an API call to get all contractsgetContracts(): Observable<Contract[]> {
  getAllContracts(): Observable<Contract[]> {
    return of(this.contracts);
  }
}
