import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { MessageService } from 'src/app/services/message.service';
import { RoutingService } from 'src/app/services/routing.service';

@Component({
  selector: 'app-request-new-password',
  templateUrl: './request-new-password.component.html'
})
export class RequestNewPasswordComponent {
  public userEmail: FormControl;

  constructor(private readonly userApiService: UserApiService,
             private readonly messageService: MessageService,
             private readonly routingService: RoutingService) { 
    this.userEmail = new FormControl('', [Validators.required, Validators.email]);
  }

  public onValidate(): void {
    this.userApiService.generateTokenForPasswordReinitialisation(this.userEmail.value).subscribe(() => {
      this.messageService.showSuccessMessage('You will receive a link in a few time');
    });
  }

  public gotoSignupComponent(): void {
    this.routingService.gotoSignupComponent();
  }

  public gotoLoginComponent(): void {
    this.routingService.gotoLoginComponent();
  }

  public get componentName(): string {
    return 'RequestNewPasswordComponent';
  }

  public get global(): string {
    return 'Global';
  }
}
