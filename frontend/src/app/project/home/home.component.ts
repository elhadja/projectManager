import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GetUserInvitationsInputDTO } from 'src/app/dto/GetUserInvitationInputDTO';
import { projectInputDTO } from 'src/app/dto/project.input.dto';
import { DialogConfirmComponent } from 'src/app/modules/shared/dialog-confirm/dialog-confirm.component';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { CreateProjectComponent } from '../dialog/create-project/create-project.component';
import { DialogProjectDetailsComponent } from '../dialog/dialog-project-details/dialog-project-details.component';
import { DialogCreateProjectService } from '../services/dialogCreateProject.service';
import { HomeService } from '../services/home.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public projects: projectInputDTO[];
  public displayedColumns: string[] = ['name', 'description', 'projectManagers', 'actions'];
  public invitations: GetUserInvitationsInputDTO[];
  public selectedInvitations: GetUserInvitationsInputDTO[];

  constructor(private router: Router,
              private homeService: HomeService,
              private matDialog: MatDialog,
              private dialogCreateProjectService: DialogCreateProjectService,
              private sessionService: sessionManagerService,
              private readonly routingService: RoutingService,
              private readonly projectApiService: ProjectApiService,
              private readonly messageService: MessageService) {
    this.projects = [];
    this.invitations = [];
    this.selectedInvitations = [];
    this.dialogCreateProjectService.onCreateProjectSuccess.subscribe(() => {
      this.closeDialogAddProject();
      this.loadUserProject();
    });
  }

  ngOnInit(): void {
    this.loadUserProject();
    this.loadUserInvitations();
  }

  private loadUserProject(): void {
    this.homeService.getUserProjects().subscribe((projects) => {
      this.projects = [...projects];
    });
  }

  private loadUserInvitations(): void {
    this.homeService.getUserProjectInvitations().subscribe((result) => {
      this.invitations = result;
    });
  }

  private refresh(): void {
    this.projects = [];
    this.invitations = [];
    this.selectedInvitations = [];

    this.ngOnInit();
  }

  public onOpenAddProjectDialog(): void {
    this.matDialog.open(CreateProjectComponent);
  }

  public onClickOnProject(projectId: number): void{
    this.sessionService.setProjectId(projectId);
    this.routingService.gotoBacklogComponent(projectId);
  }

  public onOpenProjectDetails(project: projectInputDTO): void {
    const dialogRef = this.matDialog.open(DialogProjectDetailsComponent, {
      data: {
        project
      },
      width: '50%',
      height: '95%',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.refresh();
      }
    });
  }

  public onMoveProjectToDraft(projectId: number): void {
    const project = this.projects.find(project => project.projectId === projectId);
    let message = 'You will be removed from this project.';
    if (project != null 
        && project.projectManagers.length === 1 
        && project.projectManagers[0].id === this.sessionService.getUserId()) {
      message = 'This project will be deleted permanently for all users.' +
                    'If you do not want that, you can set a new manager and then remove this project from yours.';
    } 
    this.matDialog.open(DialogConfirmComponent, {
      position: { top: '50px'},
      role: 'alertdialog',
      minWidth: '500px',
      data: { message },
    })
      .afterClosed().subscribe(accept => {
        if (accept == true) {
          this.projectApiService.removeUserFromProject(projectId, this.sessionService.getUserId()).subscribe(() => {
            this.messageService.showSuccessMessage('Project removed');
            this.refresh();
          });
        }
      });
  }

  private closeDialogAddProject(): void {
    this.matDialog.closeAll();
  }

  public selectRow(invitation: GetUserInvitationsInputDTO): void {
    console.log(invitation);
  }

  public onRowSelect(event: Event): void {
    console.log(this.selectedInvitations);
  }

  public onRowUnselect(event: Event): void {

  }

  public onAcceptInvitations(): void {
    const ids:number[] = [];
    this.selectedInvitations.forEach((invitation) => {
      ids.push(invitation.invitationToProjectId);
    });

    this.homeService.acceptInvitationToProject(ids).subscribe(() => {
      this.selectedInvitations =[];
      this.loadUserProject();
      this.loadUserInvitations();
    });
  }

  public onDeleteInvitations(): void {
    this.matDialog.open(DialogConfirmComponent).afterClosed().subscribe(accept => {
      if (accept) {
        this.deleteInvitations();
      }
    });
  }

  private deleteInvitations(): void {
    const ids:number[] = [];
    this.selectedInvitations.forEach((invitation) => {
      ids.push(invitation.invitationToProjectId);
    });

    this.homeService.cancelInvitationToProject(ids).subscribe(() => {
      this.selectedInvitations =[];
      this.loadUserInvitations();
    });
  }

  public isUserAdmin(rowData: projectInputDTO): boolean {
    return rowData.projectManagers.some(manager => this.homeService.sessionManager.getUserId() === manager.id);
  }

  public get componentName(): string {
    return 'HomeComponent';
  }

  public get projectTableName(): string {
    return `${this.componentName}.projectsTable`;
  }

  public get invitationsTableName(): string {
    return `${this.componentName}.invitationsTable`;
  }

  public get global(): string {
    return 'Global';
  }
}
