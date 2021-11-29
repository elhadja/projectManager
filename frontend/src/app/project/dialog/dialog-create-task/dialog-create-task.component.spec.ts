import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCreateTaskComponent } from './dialog-create-task.component';

describe('DialogCreateTaskComponent', () => {
  let component: DialogCreateTaskComponent;
  let fixture: ComponentFixture<DialogCreateTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogCreateTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCreateTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
