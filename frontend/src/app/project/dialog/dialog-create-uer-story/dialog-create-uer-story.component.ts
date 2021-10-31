import { Component, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AddUserStoryOutputDTO } from 'src/app/dto/addUserStoryOutputDTO';

@Component({
  selector: 'app-dialog-create-uer-story',
  templateUrl: './dialog-create-uer-story.component.html',
  styleUrls: ['./dialog-create-uer-story.component.css']
})
export class DialogCreateUerStoryComponent implements OnInit {
  public userStoryForm: FormGroup;

  constructor(private fb: FormBuilder,
             private matDialogRef: MatDialogRef<DialogCreateUerStoryComponent, AddUserStoryOutputDTO>) {
    this.userStoryForm = fb.group({
      "summary": fb.control('', [Validators.required, Validators.maxLength(100)]),
      "description": fb.control(''),
      "storyPoint": fb.control('')
    });
  }

  ngOnInit(): void {
  }

  public onValidate(): void {
    this.matDialogRef.close({
      summary: this.summary?.value,
      description: this.description?.value,
      storyPoint: this.storyPoint?.value // TODO check if story point field is valid
    });
  }

  public onCancel(): void {

  }

  public get summary () {
    return this.userStoryForm.get('summary');
  }

  public get description() {
    return this.userStoryForm.get('description');
  }

  public get storyPoint() {
    return this.userStoryForm.get('storyPoint');
  }
}
