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
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { HttpLoaderFactory } from '../app.module';
import { HttpClient } from '@angular/common/http';
import {MatMenuModule} from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { DialogProjectDetailsComponent } from './dialog/dialog-project-details/dialog-project-details.component';
import {TableModule} from 'primeng/table';
import { DialogDetailsProjectService } from './services/dialogProjectDetails.service';

const routes: Routes = [
  {path: '', canActivate: [RouteSecureService], component: HomeComponent}
]

const PRIME_NG_MODULES = [
  TableModule
]

const MAT_MODULES = [
    MatTableModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
]

@NgModule({
  declarations: [
    HomeComponent,
    CreateProjectComponent,
    DialogProjectDetailsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    [...MAT_MODULES],
    [...PRIME_NG_MODULES],
    TranslateModule.forChild({
            loader: {provide: TranslateLoader, useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
    RouterModule.forChild(routes)
  ],
  providers: [
    HomeService,
    DialogCreateProjectService,
    DialogDetailsProjectService
  ]
})
export class ProjectModule { }
