import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-dialog-confirm',
  templateUrl: './dialog-infos.component.html',
  styleUrls: []
})
export class DialogInfosComponent {

  constructor(private readonly matDialogRef: MatDialogRef<DialogInfosComponent>, 
              @Inject(MAT_DIALOG_DATA) public data: {message: Subject<string>}) {
  }

}
