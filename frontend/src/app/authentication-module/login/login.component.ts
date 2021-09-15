import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { LoginService } from '../services/login-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  emailFormControl: FormControl;
  passwordFormControl: FormControl;

  public hidePassword: boolean;
  constructor(private loginService: LoginService) { 
    this.hidePassword = true;
    // TODO add validators
    this.emailFormControl = new FormControl('');
    this.passwordFormControl = new FormControl('');
  }

  ngOnInit(): void {
  }

  public isValidForm(): boolean {
    return this.emailFormControl.valid && this.passwordFormControl.valid;
  }

  public onLogin(): void {
    this.loginService.login({
      email: this.emailFormControl.value,
      password: this.passwordFormControl.value
    });
  }

}
