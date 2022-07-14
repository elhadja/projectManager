import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RoutingService } from 'src/app/services/routing.service';
import { SignupService } from '../services/signup.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  public signupFormGroup: FormGroup;
  public hidePassword: boolean;

  constructor(private signupService: SignupService, private readonly routingService: RoutingService) {
    this.hidePassword = true;

    this.signupFormGroup = new FormGroup({
      email: new FormControl('', [Validators.email]),
      pseudo: new FormControl(''),
      password: new FormControl('')
    });
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

  public gotoLoginComponent():void {
    this.routingService.gotoLoginComponent();
  }

  public get componentName(): string {
    return 'RegisterComponent';
  }

  public get global(): string {
    return 'Global';
  }
}
