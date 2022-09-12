import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PMConstants } from 'src/app/common/PMConstants';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { SignupService } from '../services/signup.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  public signupFormGroup: FormGroup;
  public hidePassword: boolean;

  constructor(private signupService: SignupService, private readonly routingService: RoutingService, private readonly _sessionManager: sessionManagerService) {
    this.hidePassword = true;

    this.signupFormGroup = new FormGroup({
      email: new FormControl('', [Validators.email]),
      pseudo: new FormControl(''),
      password: new FormControl(''),
      lang: new FormControl(PMConstants.DEFAULT_LANG)
    });

    this.lang?.valueChanges.subscribe(value => _sessionManager.setLanguage(value));
  }

  public onSignup(): void {
    this.signupService.registeruser({
      email: this.signupFormGroup.get('email')?.value,
      pseudo: this.signupFormGroup.get('pseudo')?.value,
      password: this.signupFormGroup.get('password')?.value
    });
  }

  get email() {
    return this.signupFormGroup.get('email');
  }

  get pseudo() {
    return this.signupFormGroup.get('pseudo');
  }

  get password() {
    return this.signupFormGroup.get('password');
  }

  get lang() {
    return this.signupFormGroup.get('lang');
  }

  public gotoLoginComponent():void {
    this.routingService.gotoLoginComponent();
  }

  public get componentName(): string {
    return 'RegisterComponent';
  }

  public get global(): string {
    return 'Global';
  }

  public get languages(): {label: string, value: string}[] {
    return PMConstants.AVAILABLE_LANG;
  }
}
