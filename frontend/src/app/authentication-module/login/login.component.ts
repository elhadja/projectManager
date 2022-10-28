import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { PMConstants } from 'src/app/common/PMConstants';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { AuthenticationConstants } from '../authentication-constant';
import { LoginService } from '../services/login-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {
  userIdentifierFormControl: FormControl;
  passwordFormControl: FormControl;
  lang: FormControl;

  public readonly MIN_PASSWORD_LENGHT = AuthenticationConstants.MIN_PASSWORD_LENGTH;
  public readonly MAX_PASSWORD_LENGHT = AuthenticationConstants.MAX_PASSWORD_LENGTH;
  public readonly USER_IDENTIFIER_MIN_LENGTH = 2;
  public readonly AVAILABLE_LANGUAGES = PMConstants.AVAILABLE_LANG;

  public hidePassword: boolean;
  constructor(private loginService: LoginService,
             private routingService: RoutingService,
             private readonly sessionManagerService: sessionManagerService) { 
    if (this.sessionManagerService.isActive()) {
      routingService.gotoProjectComponent();
    }
    this.hidePassword = true;
    this.userIdentifierFormControl = new FormControl('', [Validators.required, Validators.minLength(this.USER_IDENTIFIER_MIN_LENGTH)]);
    this.passwordFormControl = new FormControl('', [Validators.required]);
    this.lang = new FormControl(sessionManagerService.getLanguage());
    this.lang.valueChanges.subscribe(val => sessionManagerService.setLanguage(val));
  }

  public get componentName(): string {
    return 'LoginComponent';
  }

  public get global(): string {
    return 'Global';
  }

  public isValidForm(): boolean {
    return this.userIdentifierFormControl.valid && this.passwordFormControl.valid;
  }

  public onLogin(): void {
    this.loginService.login({
      userIdentifier: this.userIdentifierFormControl.value,
      password: this.passwordFormControl.value,
    });
  }

  public gotoSignupComponent(): void {
    this.routingService.gotoSignupComponent();
  }

  public onForgottenPassword(): void {
    this.routingService.gotoRequestNewPasswordComponent();
  }

  public onLoginWithGoogle(): void {
    this.loginService.loginWithGoogle();
  }
}
