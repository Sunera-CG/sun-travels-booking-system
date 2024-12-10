import { Component } from '@angular/core';
import { ContractListComponent } from '../../contracts/contract-list/contract-list.component';

@Component({
  selector: 'app-homepage',
  imports: [ContractListComponent],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss',
})
export class HomepageComponent {}
