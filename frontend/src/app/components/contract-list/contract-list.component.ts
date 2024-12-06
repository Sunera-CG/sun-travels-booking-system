import { Component, OnInit } from '@angular/core';
import { Contract, ContractService } from '../../services/contract.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.scss'],
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];
  isModalOpen = false;
  selectedContract: Contract | null = null;

  constructor(private contractService: ContractService) {}

  ngOnInit(): void {
    this.contractService
      .getAllContracts()
      .subscribe((contracts: Contract[]) => {
        this.contracts = contracts;
      });
  }

  openModal(contract: Contract): void {
    this.selectedContract = contract;
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.selectedContract = null;
  }
}
