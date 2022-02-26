import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RouterModule, Routes } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { MatInputModule } from '@angular/material/input'; 
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatIconModule} from '@angular/material/icon'; 
import {MatButtonModule} from '@angular/material/button'; 
import { LoginService } from './services/login-service';
import { SignupService } from './services/signup.service';
import { HttpClient } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpLoaderFactory } from '../app.module';
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
  declarations: [
    LoginComponent,
    RegisterComponent,
    RequestNewPasswordComponent,
    UpdatePasswordComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    TranslateModule.forChild({
            loader: {provide: TranslateLoader, useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
    RouterModule.forChild(routes)
  ],
  providers: [
    LoginService,
    SignupService
  ]
})
export class AuthenticationModuleModule { }
