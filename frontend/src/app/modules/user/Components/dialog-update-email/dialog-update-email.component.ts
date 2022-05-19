import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { MessageService } from 'src/app/services/message.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-dialog-update-email',
  templateUrl: './dialog-update-email.component.html',
  styleUrls: ['./dialog-update-email.component.css']
})
export class DialogUpdateEmailComponent {
  public emailFormControl: FormControl;

  constructor(private matDialog: MatDialogRef<DialogUpdateEmailComponent>,
              private readonly userApiService: UserApiService,
              private readonly sessionManagerService: sessionManagerService,
              private readonly messageService: MessageService) { 
    this.emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  }

  public onValidate(): void {
    this.userApiService.updateEmail(this.sessionManagerService.getUserId(), this.emailFormControl.value).subscribe(() => {
      this.messageService.showSuccessMessage('You will receive an email for validating your new email');
    });
  }
}
