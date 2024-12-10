import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractSearchComponent } from './contract-search.component';

describe('ContractSearchComponent', () => {
  let component: ContractSearchComponent;
  let fixture: ComponentFixture<ContractSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractSearchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContractSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
