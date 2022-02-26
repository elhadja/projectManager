import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { RoutingService } from 'src/app/services/routing.service';
import { LoginService } from '../services/login-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  userIdentifierFormControl: FormControl;
  passwordFormControl: FormControl;

  public hidePassword: boolean;
  constructor(private loginService: LoginService, private routingService: RoutingService) { 
    this.hidePassword = true;
    this.userIdentifierFormControl = new FormControl('elhadj', [Validators.required, Validators.minLength(2)]);
    this.passwordFormControl = new FormControl('password', [Validators.required]);
  }

  ngOnInit(): void {
  }

  public isValidForm(): boolean {
    return this.userIdentifierFormControl.valid && this.passwordFormControl.valid;
  }

  public onLogin(): void {
    this.loginService.login({
      userIdentifier: this.userIdentifierFormControl.value,
      password: this.passwordFormControl.value
    });
  }

  public gotoSignupComponent(): void {
    this.routingService.gotoSignupComponent();
  }

  public onForgottenPassword(): void {
    this.routingService.gotoRequestNewPasswordComponent();
  }
}
