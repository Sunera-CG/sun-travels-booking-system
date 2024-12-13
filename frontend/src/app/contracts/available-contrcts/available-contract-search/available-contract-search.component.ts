import { Component } from '@angular/core';
import { AvailableContractViewComponent } from '../available-contract-view/available-contract-view.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AvailableContract,
  SearchContract,
} from '../../../models/available-contract.model';
import { ContractService } from '../../../services/contract.service';

@Component({
  selector: 'app-available-contract-search',
  imports: [AvailableContractViewComponent, CommonModule, FormsModule],
  templateUrl: './available-contract-search.component.html',
  styleUrl: './available-contract-search.component.scss',
})
export class AvailableContractSearchComponent {
  availableContracts: AvailableContract[] = [];
  today: string = '';
  isSubmitting: boolean = false;
  successMessage: boolean = false;
  isAvailableContractsNotFound: boolean = false;

  isChekingDateFilled: boolean = true;
  isNoOfNightsFilled: boolean = true;
  isNoOfNightsValid: boolean = true;
  isNumberOfRoomsFilled: boolean[] = [true];
  isNumberOfRoomsValid: boolean[] = [true];
  isMaxAdultsFilled: boolean[] = [true];
  isMaxAdultsValid: boolean[] = [true];
  isMaxAdultsEntered: boolean[] = [true];

  searchContract: SearchContract = {
    checkInDate: null,
    noOfNights: null,
    roomRequirements: [
      {
        numberOfRooms: null,
        maxAdults: null,
      },
    ],
  };

  constructor(private contractService: ContractService) {}
  ngOnInit(): void {
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0]; // Formats today's date as yyyy-MM-dd
  }

  addRoom() {
    this.searchContract.roomRequirements.push({
      numberOfRooms: null,
      maxAdults: null,
    });

    this.isNumberOfRoomsFilled.push(true);
    this.isNumberOfRoomsValid.push(true);
    this.isMaxAdultsFilled.push(true);
    this.isMaxAdultsValid.push(true);
    this.isMaxAdultsEntered.push(true);
  }

  removeRoom(index: number) {
    if (this.searchContract.roomRequirements.length > 1) {
      this.searchContract.roomRequirements.splice(index, 1);
      this.isNumberOfRoomsFilled.splice(index, 1);
      this.isNumberOfRoomsValid.splice(index, 1);
      this.isMaxAdultsFilled.splice(index, 1);
      this.isMaxAdultsValid.splice(index, 1);
      this.isMaxAdultsEntered.splice(index, 1);
    }
  }
  validForm(): boolean {
    this.isChekingDateFilled = !!this.searchContract.checkInDate;
    this.isNoOfNightsFilled = this.searchContract.noOfNights !== null;
    this.isNoOfNightsValid =
      this.searchContract.noOfNights !== null &&
      this.searchContract.noOfNights > 0;

    const maxAdultsTracker: Record<number, boolean> = {};

    this.searchContract.roomRequirements.forEach((room, index) => {
      this.isNumberOfRoomsFilled[index] = room.numberOfRooms !== null;
      this.isNumberOfRoomsValid[index] =
        room.numberOfRooms !== null && room.numberOfRooms > 0;
      this.isMaxAdultsFilled[index] = room.maxAdults !== null;
      this.isMaxAdultsValid[index] =
        room.maxAdults !== null && room.maxAdults > 0;
      if (
        room.maxAdults !== null &&
        maxAdultsTracker[room.maxAdults] === true
      ) {
        this.isMaxAdultsEntered[index] = false; // Mark duplicate entry
      } else {
        this.isMaxAdultsEntered[index] = true;
        if (room.maxAdults !== null) {
          maxAdultsTracker[room.maxAdults] = true; // Track this value
        }
      }
    });

    return (
      this.isChekingDateFilled &&
      this.isNoOfNightsFilled &&
      this.isNoOfNightsValid &&
      this.isMaxAdultsEntered.every(Boolean) &&
      this.isNumberOfRoomsFilled.every(Boolean) &&
      this.isNumberOfRoomsValid.every(Boolean) &&
      this.isMaxAdultsFilled.every(Boolean) &&
      this.isMaxAdultsValid.every(Boolean)
    );
  }

  submit() {
    if (this.validForm()) {
      this.isSubmitting = true;
      this.isAvailableContractsNotFound = false;
      this.contractService
        .searchAvailableContracts(this.searchContract)
        .subscribe({
          next: (availableContracts) => {
            console.log('Contracts found:', availableContracts);
            this.isSubmitting = false;
            this.successMessage = true;
            this.availableContracts = availableContracts;
            if (this.availableContracts.length === 0) {
              this.isAvailableContractsNotFound = true;
            }
          },
          error: (error) => {
            if (error.status === 400) {
              this.isAvailableContractsNotFound = true;
            }
            console.error('Error searching contracts:', error);
          },
        });
      this.searchContract = {
        checkInDate: null,
        noOfNights: null,
        roomRequirements: [
          {
            numberOfRooms: null,
            maxAdults: null,
          },
        ],
      };
    } else {
      console.log('Validation failed');
      this.availableContracts = [];
    }
  }
}
