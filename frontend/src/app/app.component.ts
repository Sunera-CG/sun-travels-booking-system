import { Component } from '@angular/core';
import { ContractListComponent } from './components/contract-list/contract-list.component';

@Component({
  selector: 'app-root',
  imports: [ContractListComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'frontend';
}
