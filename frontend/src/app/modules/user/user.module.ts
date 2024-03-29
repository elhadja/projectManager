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
import { DialogUpdateEmailComponent } from './Components/dialog-update-email/dialog-update-email.component';
import { ConfirmUpdatePasswordComponent } from './Components/confirm-update-password/confirm-update-password.component';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpLoaderFactory } from 'src/app/app.module';
import { HttpClient } from '@angular/common/http';




const routes : Routes = [
  {path: PMConstants.USER_MODULE_PROFILE, component: DialogUserProfilComponent},
  {path: PMConstants.USER_MODULE_ACCOUNT, component: AccountComponent},
  {path: PMConstants.USER_MODULE_UPDATE_EMAIL, component: ConfirmUpdatePasswordComponent},
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
    ...COMPONENTS, DialogUpdatePasswordComponent, DialogUpdateEmailComponent, ConfirmUpdatePasswordComponent
  ],
  imports: [
    ...ANGULAR_MAT_MODULES,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    TranslateModule.forChild({
      loader: {provide: TranslateLoader, useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    RouterModule.forChild(routes)
  ]
})
export class UserModule { }
