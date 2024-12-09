import { Component, Input } from '@angular/core';
import { Contract } from '../../services/contract.service';

@Component({
  selector: 'app-contract-view',
  imports: [],
  templateUrl: './contract-view.component.html',
  styleUrl: './contract-view.component.scss',
})
export class ContractViewComponent {
  @Input() contract: Contract;
  @Input() isViewPopUpOpen: boolean;

  closeViewPopUp() {
    this.isViewPopUpOpen = false; // Handle closing logic
  }
}
