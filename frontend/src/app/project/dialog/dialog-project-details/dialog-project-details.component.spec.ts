import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogProjectDetailsComponent } from './dialog-project-details.component';

describe('DialogProjectDetailsComponent', () => {
  let component: DialogProjectDetailsComponent;
  let fixture: ComponentFixture<DialogProjectDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogProjectDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogProjectDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
