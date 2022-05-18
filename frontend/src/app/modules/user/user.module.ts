import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogUserProfilComponent } from './Components/user-profile/dialog-user-profil.component';
import { RouterModule, Routes } from '@angular/router';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { PMConstants } from 'src/app/common/PMConstants';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import { AccountComponent } from './Components/account/account.component';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogUpdatePasswordComponent } from './Components/updatePassword/dialog-update-password/dialog-update-password.component';




const routes : Routes = [
  {path: PMConstants.USER_MODULE_PROFILE, component: DialogUserProfilComponent},
  {path: PMConstants.USER_MODULE_ACCOUNT, component: AccountComponent},
];

const ANGULAR_MAT_MODULES = [
  MatFormFieldModule,
  MatInputModule ,
  MatButtonModule,
  MatListModule,
  MatIconModule,
  MatDialogModule
];

const COMPONENTS = [
  DialogUserProfilComponent,
  AccountComponent,
];


@NgModule({
  declarations: [ 
    ...COMPONENTS, DialogUpdatePasswordComponent
  ],
  imports: [
    ...ANGULAR_MAT_MODULES,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterModule.forChild(routes)
  ]
})
export class UserModule { }
