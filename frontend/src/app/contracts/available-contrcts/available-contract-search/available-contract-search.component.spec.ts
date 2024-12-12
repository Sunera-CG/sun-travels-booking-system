import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableContractSearchComponent } from './available-contract-search.component';

describe('AvailableContractSearchComponent', () => {
  let component: AvailableContractSearchComponent;
  let fixture: ComponentFixture<AvailableContractSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvailableContractSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvailableContractSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
