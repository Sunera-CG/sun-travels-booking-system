import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Contract } from '../../models/contract.model';

@Component({
  selector: 'app-contract-view',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './contract-view.component.html',
  styleUrl: './contract-view.component.scss',
})
export class ContractViewComponent {
  @Input() contracts: Contract[] = [];
  @Output() removeContract: EventEmitter<number> = new EventEmitter();

  isRemovePopupOpen = false;
  isViewPopUpOpen = false;
  contractIdToRemove: number | null = null;
  selectedContract: Contract | null = null;
  status: string = '';

  displayedContracts: any[] = []; // Contracts to display on the current page
  currentPage = 1; // Current page number
  itemsPerPage = 10; // Number of contracts per page
  totalPages = 1; // Total number of pages

  ngOnInit(): void {
    this.calculatePagination();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['contracts'] && changes['contracts'].currentValue) {
      this.calculatePagination();
    }
  }

  calculatePagination(): void {
    this.totalPages = Math.ceil(this.contracts.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.displayedContracts = this.contracts.slice(startIndex, endIndex);
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.calculatePagination();
    }
  }

  nextPage(): void {
    this.changePage(this.currentPage + 1);
  }

  prevPage(): void {
    this.changePage(this.currentPage - 1);
  }

  trackByFn(index: number, item: any): any {
    return item.id || index; // Use a unique ID or index for tracking
  }

  // View contract details
  viewContract(contract: Contract): void {
    this.selectedContract = contract;
    const currentDate = new Date();

    if (this.selectedContract.endDate && this.selectedContract.startDate) {
      const startDate = new Date(this.selectedContract.startDate);
      const endDate = new Date(this.selectedContract.endDate);

      if (endDate >= currentDate && startDate <= currentDate) {
        this.status = 'active';
      } else if (endDate >= currentDate && startDate > currentDate) {
        this.status = 'up coming';
      } else {
        this.status = 'expired';
      }
    }
    this.isViewPopUpOpen = true;
  }

  // Close the contract view popup
  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedContract = null;
    this.status = '';
  }

  // Open the confirmation popup for contract removal
  openRemoveConfirmation(contract: Contract): void {
    this.isRemovePopupOpen = true;
    this.contractIdToRemove = contract.contractId;
  }

  // Confirm the contract removal
  confirmRemoveContract(): void {
    if (this.contractIdToRemove !== null) {
      this.removeContract.emit(this.contractIdToRemove);
    }
    this.closeRemovePopup();
  }
  // Cancel the removal and close the popup
  cancelRemoveContract(): void {
    this.closeRemovePopup();
  }

  // Close the remove confirmation popup
  closeRemovePopup(): void {
    this.isRemovePopupOpen = false;
    this.contractIdToRemove = null;
  }
}
