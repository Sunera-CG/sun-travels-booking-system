import { Component } from '@angular/core';
import { HeaderComponent } from '../../shared/header/header.component';
import { FooterComponent } from '../../shared/footer/footer.component';
import { AvailableContractSearchComponent } from '../../contracts/available-contrcts/available-contract-search/available-contract-search.component';

@Component({
  selector: 'app-searchpage',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, AvailableContractSearchComponent],
  templateUrl: './searchpage.component.html',
  styleUrl: './searchpage.component.scss',
})
export class SearchpageComponent {}
