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
  isSubmitting: boolean = false;
  successMessage: boolean = false;

  isHotelNameFilled: boolean = true;
  isStartDateFilled: boolean = true;
  isEndDateFilled: boolean = true;
  isEndDateBeforeStartDate: boolean = true;
  isMarkUpRateFilled: boolean = true;
  isMarkUpRateValid: boolean = true;
  isRoomTypeFilled: boolean[] = [true];
  isPricePerPersonFilled: boolean[] = [true];
  isPricePerPersonValid: boolean[] = [true];
  isNumberOfRoomsFilled: boolean[] = [true];
  isNumberOfRoomsValid: boolean[] = [true];
  isMaxAdultsFilled: boolean[] = [true];
  isMaxAdultsValid: boolean[] = [true];

  // New contract object to bind to the form
  newContract: Contract = {
    contractId: null,
    hotelName: '',
    startDate: null,
    endDate: null,
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

  /**
   * Initializes the component and sets today's date in the required format.
   */
  ngOnInit(): void {
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0]; // Formats today's date as yyyy-MM-dd
  }

  /**
   * Adds a new room to the contract room details.
   * Also adds validation arrays for the new room.
   */
  addRoom() {
    this.newContract.roomDetails.push({
      roomType: '',
      pricePerPerson: null,
      numberOfRooms: null,
      maxAdults: null,
    });

    this.isRoomTypeFilled.push(true);
    this.isPricePerPersonFilled.push(true);
    this.isPricePerPersonValid.push(true);
    this.isNumberOfRoomsFilled.push(true);
    this.isNumberOfRoomsValid.push(true);
    this.isMaxAdultsFilled.push(true);
    this.isMaxAdultsValid.push(true);
  }

  /**
   * Emits an event to close the contract add form.
   */
  closeAddContract(): void {
    this.closeForm.emit();
  }

  /**
   * Removes a room detail from the contract.
   * @param index - The index of the room to remove.
   */
  removeRoom(index: number) {
    if (this.newContract.roomDetails.length > 1) {
      this.newContract.roomDetails.splice(index, 1);
      this.isRoomTypeFilled.splice(index, 1);
      this.isPricePerPersonFilled.splice(index, 1);
      this.isPricePerPersonValid.splice(index, 1);
      this.isNumberOfRoomsFilled.splice(index, 1);
      this.isNumberOfRoomsValid.splice(index, 1);
      this.isMaxAdultsFilled.splice(index, 1);
      this.isMaxAdultsValid.splice(index, 1);
    }
  }

  /**
   * Validates the form fields and returns a boolean indicating whether the form is valid.
   * @returns {boolean} Returns true if the form is valid, otherwise false.
   */
  validateForm(): boolean {
    this.isHotelNameFilled = !!this.newContract.hotelName.trim();
    this.isStartDateFilled = !!this.newContract.startDate;
    this.isEndDateFilled = !!this.newContract.endDate;
    this.isEndDateBeforeStartDate =
      this.newContract.startDate && this.newContract.endDate
        ? this.newContract.endDate >= this.newContract.startDate
        : true;
    this.isMarkUpRateFilled = this.newContract.markUpRate !== null;
    this.isMarkUpRateValid =
      this.newContract.markUpRate !== null &&
      this.newContract.markUpRate >= 1 &&
      this.newContract.markUpRate <= 100;

    this.newContract.roomDetails.forEach((room, index) => {
      this.isRoomTypeFilled[index] = !!room.roomType.trim();
      this.isPricePerPersonFilled[index] = room.pricePerPerson !== null;
      this.isPricePerPersonValid[index] =
        room.pricePerPerson !== null && room.pricePerPerson >= 0;
      this.isNumberOfRoomsFilled[index] = room.numberOfRooms !== null;
      this.isNumberOfRoomsValid[index] =
        room.numberOfRooms !== null && room.numberOfRooms > 0;
      this.isMaxAdultsFilled[index] = room.maxAdults !== null;
      this.isMaxAdultsValid[index] =
        room.maxAdults !== null && room.maxAdults > 0;
    });

    return (
      this.isHotelNameFilled &&
      this.isStartDateFilled &&
      this.isEndDateFilled &&
      this.isEndDateBeforeStartDate &&
      this.isMarkUpRateFilled &&
      this.isMarkUpRateValid &&
      this.isRoomTypeFilled.every(Boolean) &&
      this.isPricePerPersonFilled.every(Boolean) &&
      this.isPricePerPersonValid.every(Boolean) &&
      this.isNumberOfRoomsFilled.every(Boolean) &&
      this.isNumberOfRoomsValid.every(Boolean) &&
      this.isMaxAdultsFilled.every(Boolean) &&
      this.isMaxAdultsValid.every(Boolean)
    );
  }

  /**
   * Submits the contract if the form is valid and resets the contract form.
   */
  submit() {
    console.log('Contract Data', this.newContract);
    console.log('Room Details:', this.newContract.roomDetails);
    if (this.validateForm()) {
      this.isSubmitting = true;
      this.submitContract.emit(this.newContract);
      this.newContract = {
        contractId: null,
        hotelName: '',
        startDate: null,
        endDate: null,
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
      this.successMessage = true;

      setTimeout(() => {
        this.successMessage = false;
      }, 3000);

      this.closeAddContract();
    }
  }
}
