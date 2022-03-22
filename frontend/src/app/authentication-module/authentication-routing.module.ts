import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RequestNewPasswordComponent } from './request-new-password/request-new-password.component';
import { UpdatePasswordComponent } from './update-password/update-password.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: RegisterComponent},
  { path: 'password-request', component: RequestNewPasswordComponent },
  { path: 'password-update', component: UpdatePasswordComponent },
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class AuthenticationRoutingModule {}