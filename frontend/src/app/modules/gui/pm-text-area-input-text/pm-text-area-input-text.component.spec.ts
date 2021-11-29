import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PmTextAreaInputTextComponent } from './pm-text-area-input-text.component';

describe('PmTextAreaInputTextComponent', () => {
  let component: PmTextAreaInputTextComponent;
  let fixture: ComponentFixture<PmTextAreaInputTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PmTextAreaInputTextComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PmTextAreaInputTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
