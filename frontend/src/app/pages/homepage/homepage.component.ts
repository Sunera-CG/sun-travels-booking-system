import { Component } from '@angular/core';
import { ContractListComponent } from '../../contracts/contract-list/contract-list.component';
import { HeaderComponent } from '../../shared/header/header.component';
import { FooterComponent } from '../../shared/footer/footer.component';

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [ContractListComponent, HeaderComponent, FooterComponent],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss',
})
export class HomepageComponent {}
