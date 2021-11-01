import { Component, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { PMConstants } from 'src/app/common/PMConstants';
import { AddUserStoryOutputDTO } from 'src/app/dto/addUserStoryOutputDTO';

interface LabelValue {
  value: string,
  label: string
}

@Component({
  selector: 'app-dialog-create-uer-story',
  templateUrl: './dialog-create-uer-story.component.html',
  styleUrls: ['./dialog-create-uer-story.component.css']
})
export class DialogCreateUerStoryComponent implements OnInit {
  public userStoryForm: FormGroup;
  public userStoryImportanceValues: LabelValue[];

  constructor(private fb: FormBuilder,
             private matDialogRef: MatDialogRef<DialogCreateUerStoryComponent, AddUserStoryOutputDTO>) {
    this.userStoryForm = fb.group({
      "summary": fb.control('', [Validators.required, Validators.maxLength(200)]),
      "description": fb.control(''),
      "storyPoint": fb.control(''),
      "importance": fb.control('')
    });
    this.userStoryImportanceValues = [];
  }

  ngOnInit(): void {
    this.userStoryImportanceValues = [
      {value: PMConstants.USER_STORY_STATUS_IMPORTANCE_HIGHT, label: 'hight'},
      {value: PMConstants.USER_STORY_STATUS_IMPORTANCE_NORMAL, label: 'normal'},
      {value: PMConstants.USER_STORY_STATUS_IMPORTANCE_LOW, label: 'low'}
    ]
  }

  public onValidate(): void {
    this.matDialogRef.close({
      summary: this.summary?.value,
      description: this.description?.value,
      storyPoint: this.storyPoint?.value,
      importance: this.importance?.value
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

  public get importance() {
    return this.userStoryForm.get('importance');
  }
}
