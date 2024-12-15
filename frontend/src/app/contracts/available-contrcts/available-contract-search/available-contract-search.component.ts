import { Component } from '@angular/core';
import { AvailableContractViewComponent } from '../available-contract-view/available-contract-view.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  AvailableContract,
  SearchContract,
} from '../../../models/available-contract.model';
import { ContractService } from '../../../services/contract.service';

/**
 * Component responsible for searching available contracts based on user input.
 * It includes functionality for adding/removing rooms, validating the form,
 * and submitting the search request to the contract service.
 */
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
  lastSearchContract: SearchContract | undefined;

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

  /**
   * Lifecycle hook that sets today's date when the component is initialized.
   * @return void
   */
  ngOnInit(): void {
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0]; // Formats today's date as yyyy-MM-dd
  }

  /**
   * Adds a new room to the search contract with empty fields.
   * @return void
   */
  addRoom() {
    this.searchContract.roomRequirements.push({
      numberOfRooms: null,
      maxAdults: null,
    });

    // Adding validation flags for the new room
    this.isNumberOfRoomsFilled.push(true);
    this.isNumberOfRoomsValid.push(true);
    this.isMaxAdultsFilled.push(true);
    this.isMaxAdultsValid.push(true);
    this.isMaxAdultsEntered.push(true);
  }

  /**
   * Removes a room from the search contract based on the provided index.
   * @param index The index of the room to remove
   * @return void
   */
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

  /**
   * Validates the search contract form.
   * Checks if required fields are filled and if the values are valid.
   * @return boolean True if the form is valid, false otherwise
   */
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

  /**
   * Submits the search request for available contracts.
   * If the form is valid, it triggers a request to search for available contracts.
   * @return void
   */
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
          },
          error: (error) => {
            if (error.status === 400) {
              this.isAvailableContractsNotFound = true;
            }
            this.isSubmitting = false;
            console.error('Error searching contracts:', error);
          },
        });

      this.lastSearchContract = { ...this.searchContract };
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
