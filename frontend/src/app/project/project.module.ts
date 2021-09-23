import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { RouterModule, Routes } from '@angular/router';
import { RouteSecureService } from '../services/routeSecure.service';
import {MatTableModule} from '@angular/material/table'; 
import { HomeService } from './services/home.service';
import { CreateProjectComponent } from './dialog/create-project/create-project.component';
import {MatFormFieldModule} from '@angular/material/form-field'; 
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatDialogModule} from '@angular/material/dialog';
import { DialogCreateProjectService } from './services/dialogCreateProject.service';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

const routes: Routes = [
  {path: '', canActivate: [RouteSecureService], component: HomeComponent}
]

@NgModule({
  declarations: [
    HomeComponent,
    CreateProjectComponent
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    RouterModule.forChild(routes)
  ],
  providers: [
    HomeService,
    DialogCreateProjectService
  ]
})
export class ProjectModule { }
