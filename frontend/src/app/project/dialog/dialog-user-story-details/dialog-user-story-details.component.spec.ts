import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogUserStoryDetailsComponent } from './dialog-user-story-details.component';

describe('DialogUserStoryDetailsComponent', () => {
  let component: DialogUserStoryDetailsComponent;
  let fixture: ComponentFixture<DialogUserStoryDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogUserStoryDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogUserStoryDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
