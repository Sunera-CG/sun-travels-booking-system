import { Component, Input } from '@angular/core';
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

  isViewPopUpOpen = false;
  selectedContract: Contract | null = null;

  viewContract(contract: Contract): void {
    this.selectedContract = contract;
    this.isViewPopUpOpen = true;
  }

  // Close the contract view popup
  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedContract = null;
  }
}
