import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { PMConstants } from 'src/app/common/PMConstants';
import { projectInputDTO } from 'src/app/dto/project.input.dto';
import { CreateProjectComponent } from '../dialog/create-project/create-project.component';
import { DialogCreateProjectService } from '../services/dialogCreateProject.service';
import { HomeService } from '../services/home.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public projects: projectInputDTO[];
  public displayedColumns: string[] = ['name', 'description', 'projectManagers'];

  constructor(private router: Router,
              private homeService: HomeService,
              private addProjectDialog: MatDialog,
              private dialogCreateProjectService: DialogCreateProjectService) {
    this.projects = [];
    this.dialogCreateProjectService.onCreateProjectSuccess.subscribe(() => {
      // TODO refactor
      addProjectDialog.closeAll();
      this.homeService.getUserProjects().subscribe((projects) => {
        this.projects = [...projects];
      })
    })
  }

  ngOnInit(): void {
    this.homeService.getUserProjects().subscribe((projects) => {
      this.projects = [...projects];
    })
  }

  public onLogout(): void {
    localStorage.removeItem('token');
    this.router.navigateByUrl(PMConstants.AUTHENTICATION_MODULE_BASE_URI);
  }

  public onOpenAddProjectDialog(): void {
    this.addProjectDialog.open(CreateProjectComponent);
  }
}
