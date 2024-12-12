import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailableContractViewComponent } from './available-contract-view.component';

describe('AvailableContractViewComponent', () => {
  let component: AvailableContractViewComponent;
  let fixture: ComponentFixture<AvailableContractViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AvailableContractViewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AvailableContractViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
