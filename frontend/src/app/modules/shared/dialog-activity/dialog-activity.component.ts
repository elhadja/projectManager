import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subject } from 'rxjs';
import { CustomRevisionEntityDTO } from 'src/app/dto/custom-revision-entity.dto';

@Component({
  selector: 'app-dialog-activity',
  templateUrl: './dialog-activity.component.html',
  styleUrls: []
})
export class DialogActivityComponent {
  public activities: CustomRevisionEntityDTO[];

  constructor(private readonly MatDialogRef: MatDialogRef<DialogActivityComponent>,
            @Inject(MAT_DIALOG_DATA) public dialogData: { activities: Subject<CustomRevisionEntityDTO[]>}) { 
    this.activities = [];

    this.dialogData.activities.subscribe(_activities => this.activities = [..._activities]);
  }

}
