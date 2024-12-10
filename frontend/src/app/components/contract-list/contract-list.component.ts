import { Component, OnInit } from '@angular/core';
import { Contract, ContractService } from '../../services/contract.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contract-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contract-list.component.html',
  styleUrls: ['./contract-list.component.scss'],
})
export class ContractListComponent implements OnInit {
  contracts: Contract[] = [];
  filteredContracts: Contract[] = [];
  isViewPopUpOpen = false;
  isAddContractOpen = false;
  selectedContract: Contract | null = null;

  isHotelNameFilled = false;
  isStartDateFilled = false;
  isEndDateFilled = false;
  isMarkupRateFilled = false;

  // Search term for filtering contracts
  searchTerm: string = '';

  // New contract object to bind to the form
  newContract: Contract = {
    hotelName: '',
    startDate: new Date(),
    endDate: new Date(),
    markUpRate: 0,
    roomDetails: [],
  };

  constructor(private contractService: ContractService) {}

  ngOnInit(): void {
    this.contractService
      .getAllContracts()
      .subscribe((contracts: Contract[]) => {
        this.contracts = contracts;
        this.filteredContracts = contracts;
      });
  }

  searchContract(): void {
    if (this.searchTerm.trim() === '') {
      // If search term is empty, show all contracts
      this.filteredContracts = this.contracts;
    } else {
      this.filteredContracts = this.contracts.filter((contract) =>
        contract.hotelName.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    }
  }

  openViewPopUp(contract: Contract): void {
    this.selectedContract = contract;
    this.isViewPopUpOpen = true;
  }

  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedContract = null;
  }

  openAddContract(): void {
    this.isAddContractOpen = true;
  }

  // Add the new contract
  closeAddContract(): void {
    this.isAddContractOpen = false;
    this.newContract = {
      hotelName: '',
      startDate: new Date(),
      endDate: new Date(),
      markUpRate: 0,
      roomDetails: [],
    };
  }

  // Function to add a new room detail
  addRoom() {
    this.newContract.roomDetails.push({
      roomType: '',
      pricePerPerson: 0,
      numberOfRooms: 0,
      maxAdults: 0,
    });
  }

  submitAddContract(): void {
    console.log('Submitting contract:', this.newContract);
    this.contractService.addContract(this.newContract).subscribe({
      next: (response) => {
        console.log('Contract added successfully:', response);
        this.contracts.push(response);
        this.closeAddContract();
      },
      error: (error) => {
        console.error('Error adding contract:', error);
      },
    });
  }
}
