import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contract } from '../models/contract.model';

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

  searchContracts(searchTerm: string): Observable<Contract[]> {
    const searchUrl = `${this.apiUrl}/${searchTerm}`;
    return this.http.get<Contract[]>(searchUrl);
  }
}
