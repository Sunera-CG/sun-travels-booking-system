import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contract-search',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './contract-search.component.html',
  styleUrl: './contract-search.component.scss',
})
export class ContractSearchComponent {
  searchTerm: string = '';
  @Output() search = new EventEmitter<string>();
  @Output() openAddContract = new EventEmitter<void>();

  searchContract() {
    this.search.emit(this.searchTerm);
  }
}
