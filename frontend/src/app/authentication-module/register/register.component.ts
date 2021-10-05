import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SignupService } from '../services/signup.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public signupFormGroup: FormGroup;
  public hidePassword: boolean;

  constructor(private signupService: SignupService) {
    this.hidePassword = false;

    this.signupFormGroup = new FormGroup({
      email: new FormControl('', [Validators.email]),
      pseudo: new FormControl(''),
      password: new FormControl('')
    });
   }

  ngOnInit(): void {
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
}
