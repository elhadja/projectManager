import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogCreateUerStoryComponent } from './dialog-create-uer-story.component';

describe('DialogCreateUerStoryComponent', () => {
  let component: DialogCreateUerStoryComponent;
  let fixture: ComponentFixture<DialogCreateUerStoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogCreateUerStoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DialogCreateUerStoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
