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

  // Group rooms by requirementId
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

  getRoomRequirement(requirementId: number) {
    return this.searchContract?.roomRequirements[requirementId - 1]; // Assuming requirementId starts from 1
  }
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

  // View availableContract details
  viewContract(availableContract: AvailableContract): void {
    this.selectedAvailableContract = availableContract;
    this.isViewPopUpOpen = true;
    this.groupRoomsByRequirementId();
    console.log(this.searchContract);
  }

  // Close the contract view popup
  closeViewPopUp(): void {
    this.isViewPopUpOpen = false;
    this.selectedAvailableContract = null;
  }
}
