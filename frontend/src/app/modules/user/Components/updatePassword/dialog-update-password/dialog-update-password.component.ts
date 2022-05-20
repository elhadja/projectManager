import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { MessageService } from 'src/app/services/message.service';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-dialog-update-password',
  templateUrl: './dialog-update-password.component.html',
  styleUrls: ['./dialog-update-password.component.css']
})
export class DialogUpdatePasswordComponent {

  public readonly componentName = 'updatePassswordComponent';

  public password: FormControl;
  public confirmPassword: FormControl;
  public hidePassword: boolean;

  constructor(private readonly dialogRef: MatDialogRef<DialogUpdatePasswordComponent>,
              private readonly userApiService: UserApiService,
              private readonly messageService: MessageService,
              private readonly sessionManagerService: sessionManagerService) {
    this.password = new FormControl('', [Validators.required]);
    this.confirmPassword = new FormControl('', [Validators.required]);
    this.hidePassword = true;
  }

  public onValidate(): void {
    if (this.password.value !== this.confirmPassword.value) {
      this.messageService.showErrorMessage('Password are differents');
      return;
    }
    this.userApiService.updatePassword({
      password: this.password.value ?? '',
      confirmedPassword: this.confirmPassword.value ?? ''
    }, this.sessionManagerService.getUserId()).subscribe(() => {
      this.messageService.showSuccessMessage('Password updated');
      this.dialogRef.close();
    });
  }


}
