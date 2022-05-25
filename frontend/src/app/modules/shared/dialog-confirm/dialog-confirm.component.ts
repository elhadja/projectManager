import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-dialog-confirm',
  templateUrl: './dialog-confirm.component.html',
  styleUrls: []
})
export class DialogConfirmComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: {message: string},
              private readonly matDialogRef: MatDialogRef<DialogConfirmComponent>) {}

  public onAccept(): void {
    this.matDialogRef.close(true);
  }

  public onReject(): void {
    this.matDialogRef.close(false);
  }

}
