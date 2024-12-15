import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Contract } from '../../models/contract.model';

/**
 * Component to display a list of contracts with functionalities such as sorting, pagination,
 * and viewing contract details. It also handles contract removal confirmation and viewing statuses.
 */
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
  sortField: string = 'contractId'; // Default sort field

  ngOnInit(): void {
    this.calculatePagination();
  }

  /**
   * Lifecycle hook that reacts to changes in the input contracts and updates pagination.
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['contracts'] && changes['contracts'].currentValue) {
      this.calculatePagination();
    }
  }

  /**
   * Handles the sorting field change by the user.
   * @param event The change event from the select dropdown.
   */
  onSortChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement; // Explicit type assertion
    this.sortField = selectElement.value; // Access the value safely
    this.sortContracts();
    this.currentPage = 1; // Reset to the first page
    this.calculatePagination(); // Update displayed contracts
  }

  /**
   * Sorts the contracts based on the selected field and order.
   */
  sortContracts(): void {
    this.contracts.sort((a, b) => {
      switch (this.sortField) {
        case 'hotelName':
          return a.hotelName.localeCompare(b.hotelName);
        case 'endDate':
          return (
            new Date(b.endDate ?? 0).getTime() -
            new Date(a.endDate ?? 0).getTime()
          );
        case 'contractId':
          return (a.contractId ?? 0) - (b.contractId ?? 0); // Fallback to 0 if null
        default:
          return 0;
      }
    });
  }

  /**
   * Calculates pagination by determining which contracts to display based on the current page.
   */
  calculatePagination(): void {
    this.totalPages = Math.ceil(this.contracts.length / this.itemsPerPage);
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.displayedContracts = this.contracts.slice(startIndex, endIndex);
  }

  /**
   * Changes the page number for pagination.
   * @param page The page number to change to.
   */
  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.calculatePagination();
    }
  }

  /**
   * Navigates to the next page in the pagination.
   */
  nextPage(): void {
    this.changePage(this.currentPage + 1);
  }

  /**
   * Navigates to the previous page in the pagination.
   */
  prevPage(): void {
    this.changePage(this.currentPage - 1);
  }

  /**
   * A trackBy function to optimize Angular's rendering by using unique contractId for each contract.
   * @param index The index of the contract in the list.
   * @param item The contract being iterated.
   * @returns The unique identifier for the contract (contractId).
   */
  trackByFn(index: number, item: any): any {
    return item.id || index; // Use a unique ID or index for tracking
  }

  /**
   * Opens the contract view popup and determines the status of the contract.
   * @param contract The contract to view details.
   */
  viewContract(contract: Contract): void {
    this.selectedContract = contract;
    const currentDate = new Date();

    // Refactored status determination based on contract dates
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

  /**
   * Closes the contract view popup and resets selected contract and status.
   */
  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedContract = null;
    this.status = '';
  }

  /**
   * Opens the remove confirmation popup for a specific contract.
   * @param contract The contract to be removed.
   */
  openRemoveConfirmation(contract: Contract): void {
    this.isRemovePopupOpen = true;
    this.contractIdToRemove = contract.contractId;
  }

  /**
   * Confirms the contract removal and emits the removal event.
   */
  confirmRemoveContract(): void {
    if (this.contractIdToRemove !== null) {
      this.removeContract.emit(this.contractIdToRemove);
    }
    this.closeRemovePopup();
  }

  /**
   * Cancels the contract removal and closes the confirmation popup.
   */
  cancelRemoveContract(): void {
    this.closeRemovePopup();
  }

  /**
   * Closes the remove confirmation popup and resets the contract ID to remove.
   */
  closeRemovePopup(): void {
    this.isRemovePopupOpen = false;
    this.contractIdToRemove = null;
  }
}
