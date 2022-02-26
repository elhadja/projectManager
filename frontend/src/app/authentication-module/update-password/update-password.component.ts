import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent {
  public readonly componentName = 'updatePassswordComponent';

  public password: FormControl;
  public confirmPassword: FormControl;
  public hidePassword: boolean;

  constructor(private readonly userApiService: UserApiService,
              private readonly messageService: MessageService,
              private readonly route: ActivatedRoute) {
    this.password = new FormControl('', [Validators.required]);
    this.confirmPassword = new FormControl('', [Validators.required]);
    this.hidePassword = true;
  }

  public onValidate(): void {
    if (this.password.value !== this.confirmPassword.value) {
      this.messageService.showErrorMessage('Password are differents');
      return;
    }
    const token = this.route.snapshot.queryParamMap.get('token');
    this.userApiService.reinitializePassword(this.password.value, token!).subscribe(() => {
      this.messageService.showSuccessMessage('Your password has been changed');
    });
  }
}
