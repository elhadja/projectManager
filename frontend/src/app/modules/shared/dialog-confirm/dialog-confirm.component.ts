import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-dialog-confirm',
  templateUrl: './dialog-confirm.component.html',
  styleUrls: ['./dialog-confirm.component.css']
})
export class DialogConfirmComponent {

  constructor(private readonly matDialogRef: MatDialogRef<DialogConfirmComponent>, 
              @Inject(MAT_DIALOG_DATA) public data: {message: Subject<string>}) {
  }

}
