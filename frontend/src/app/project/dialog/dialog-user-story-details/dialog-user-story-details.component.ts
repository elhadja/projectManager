import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-user-story-details',
  templateUrl: './dialog-user-story-details.component.html',
  styleUrls: ['./dialog-user-story-details.component.css']
})
export class DialogUserStoryDetailsComponent implements OnInit {

  constructor(private readonly matDialogRef: MatDialogRef<DialogUserStoryDetailsComponent>) { }

  ngOnInit(): void {
  }

}
