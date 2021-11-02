import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCreateSprintComponent } from './dialog-create-sprint.component';

describe('DialogCreateSprintComponent', () => {
  let component: DialogCreateSprintComponent;
  let fixture: ComponentFixture<DialogCreateSprintComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogCreateSprintComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCreateSprintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
