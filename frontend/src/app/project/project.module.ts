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
import {MatTabsModule} from '@angular/material/tabs';
import { BacklogComponent } from './backlog/backlog.component';
import { DialogCreateUerStoryComponent } from './dialog/dialog-create-uer-story/dialog-create-uer-story.component';
import { BacklogService } from './services/backlog.service';
import { DialogCreateSprintComponent } from './dialog/dialog-create-sprint/dialog-create-sprint.component';
import { DialogUserStoryDetailsComponent } from './dialog/dialog-user-story-details/dialog-user-story-details.component';
import {BadgeModule} from 'primeng/badge';
import { MatSelectModule } from '@angular/material/select';
import { CalendarModule } from 'primeng/calendar';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { TaskComponent } from './task/task.component'
import { GuiModule } from '../modules/gui/gui.module';
import { DialogCreateTaskComponent } from './dialog/dialog-create-task/dialog-create-task.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {MatListModule} from '@angular/material/list';
import {MatCardModule} from '@angular/material/card';



const routes: Routes = [
  {path: '', canActivate: [RouteSecureService], component: HomeComponent},
  {path: 'backlog/:backlog-id', canActivate: [RouteSecureService], component: BacklogComponent},
  {path: 'task/:project-id', canActivate: [RouteSecureService], component: TaskComponent}
]

const PRIME_NG_MODULES = [
  TableModule,
  BadgeModule,
  CalendarModule,
  OverlayPanelModule
]

const PM_MODULE = [
  GuiModule
]

const MAT_MODULES = [
    MatTableModule,
    MatFormFieldModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
    MatMenuModule,
    MatIconModule,
    MatTabsModule,
    MatSelectModule,
    MatButtonToggleModule,
    DragDropModule,
    MatListModule,
    MatCardModule
]

@NgModule({
  declarations: [
    HomeComponent,
    CreateProjectComponent,
    DialogProjectDetailsComponent,
    BacklogComponent,
    DialogCreateUerStoryComponent,
    DialogCreateSprintComponent,
    DialogUserStoryDetailsComponent,
    TaskComponent,
    DialogCreateTaskComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    [...PM_MODULE],
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
    DialogDetailsProjectService,
    BacklogService
  ]
})
export class ProjectModule { }
