import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AddSprintToProjectOutputDTO } from 'src/app/dto/addSprintToProjectOutputDTO';

@Component({
  selector: 'app-dialog-create-sprint',
  templateUrl: './dialog-create-sprint.component.html',
  styleUrls: ['./dialog-create-sprint.component.css']
})
export class DialogCreateSprintComponent implements OnInit {
  public sprintForm: FormGroup;

  constructor(private readonly fb: FormBuilder,
             private dialogRef: MatDialogRef<DialogCreateSprintComponent, AddSprintToProjectOutputDTO>) {
    this.sprintForm = fb.group({
      'name': fb.control('', [Validators.required, Validators.minLength(2)])
    });
   }

  ngOnInit(): void {
  }

  public onValidate(): void {
    this.dialogRef.close({
      name: this.name?.value
    });
  }

  public get name() {
    return this.sprintForm.get('name');
  }

}
