import { Component, Input } from '@angular/core';
import {
  AvailableContract,
  AvailableRoomDetail,
  SearchContract,
} from '../../../models/available-contract.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-available-contract-view',
  imports: [CommonModule, FormsModule],
  templateUrl: './available-contract-view.component.html',
  styleUrl: './available-contract-view.component.scss',
})
export class AvailableContractViewComponent {
  @Input() availableContracts: AvailableContract[] = [];
  @Input() searchContract?: SearchContract;
  @Input() isAvailableContractsNotFound: boolean = false;

  isViewPopUpOpen = false;
  selectedAvailableContract: AvailableContract | null = null;

  // To store the rooms grouped by requirementId
  groupedRooms: Map<number, AvailableRoomDetail[]> = new Map();

  /**
   * Groups the rooms by their requirementId.
   * This method organizes the available rooms into a Map where the keys are requirementIds,
   * and the values are arrays of AvailableRoomDetail objects.
   */
  groupRoomsByRequirementId(): void {
    if (this.selectedAvailableContract) {
      const rooms = this.selectedAvailableContract.availableRooms;
      this.groupedRooms.clear();

      rooms.forEach((room) => {
        if (!this.groupedRooms.has(room.requirementId)) {
          this.groupedRooms.set(room.requirementId, []);
        }
        this.groupedRooms.get(room.requirementId)?.push(room);
      });
    }
  }

  /**
   * Returns the room requirement for a given requirementId.
   * @param requirementId - The ID of the room requirement to fetch.
   * @return The room requirement corresponding to the given requirementId.
   */

  getRoomRequirement(requirementId: number) {
    return this.searchContract?.roomRequirements[requirementId - 1]; // Assuming requirementId starts from 1
  }

  /**
   * Adds a specified number of days to a given date.
   * @param date - The initial date to which days will be added.
   * @param days - The number of days to add to the date.
   * @return A new Date object with the days added, or null if the inputs are invalid.
   */
  addDays(
    date: Date | null | undefined,
    days: number | null | undefined
  ): Date | null {
    if (!date || days == null) {
      return null; // Return null if the date or days are invalid (null or undefined)
    }

    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }

  /**
   * Opens a pop-up to view the details of a selected contract.
   * @param availableContract - The contract to display details for.
   */
  viewContract(availableContract: AvailableContract): void {
    this.selectedAvailableContract = availableContract;
    this.isViewPopUpOpen = true;
    this.groupRoomsByRequirementId();
    console.log(this.searchContract);
  }

  /**
   * Closes the contract view pop-up.
   */
  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedAvailableContract = null;
  }
}
