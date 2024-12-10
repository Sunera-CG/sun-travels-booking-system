import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

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

@Injectable({
  providedIn: 'root',
})
export class ContractService {
  private apiUrl = 'http://localhost:8080/contracts';

  constructor(private http: HttpClient) {}

  addContract(contract: Contract): Observable<Contract> {
    return this.http.post<Contract>(this.apiUrl, contract);
  }

  getAllContracts(): Observable<Contract[]> {
    return this.http.get<Contract[]>(this.apiUrl);
  }
}
