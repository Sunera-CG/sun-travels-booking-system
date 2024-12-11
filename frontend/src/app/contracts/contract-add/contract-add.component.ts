import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Contract } from '../../models/contract.model';

@Component({
  selector: 'app-contract-add',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './contract-add.component.html',
  styleUrl: './contract-add.component.scss',
})
export class ContractAddComponent {
  @Input() isAddContractOpen: boolean = false;
  @Output() submitContract = new EventEmitter<Contract>();
  @Output() closeForm = new EventEmitter<void>();

  today: string = '';

  ngOnInit(): void {
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0]; // Formats today's date as yyyy-MM-dd
  }

  isHotelNameFilled = false;
  isStartDateFilled = false;
  isEndDateFilled = false;
  isMarkupRateFilled = false;

  // New contract object to bind to the form
  newContract: Contract = {
    contractId: null,
    hotelName: '',
    startDate: new Date(),
    endDate: new Date(),
    markUpRate: null,
    roomDetails: [
      {
        roomType: '',
        pricePerPerson: null,
        numberOfRooms: null,
        maxAdults: null,
      },
    ],
  };

  // Function to add a new room detail
  addRoom() {
    this.newContract.roomDetails.push({
      roomType: '',
      pricePerPerson: null,
      numberOfRooms: null,
      maxAdults: null,
    });
  }

  // Add the new contract
  closeAddContract(): void {
    this.closeForm.emit();
  }

  // Function to remove a room detail
  removeRoom(index: number) {
    if (this.newContract.roomDetails.length > 1) {
      this.newContract.roomDetails.splice(index, 1);
    }
  }

  submit() {
    console.log('Contract Data', this.newContract);
    console.log('Room Details:', this.newContract.roomDetails);

    this.submitContract.emit(this.newContract);
    this.newContract = {
      contractId: null,
      hotelName: '',
      startDate: new Date(),
      endDate: new Date(),
      markUpRate: 0,
      roomDetails: [
        {
          roomType: '',
          pricePerPerson: 0,
          numberOfRooms: 0,
          maxAdults: 0,
        },
      ],
    };
    this.closeAddContract();
  }
}
