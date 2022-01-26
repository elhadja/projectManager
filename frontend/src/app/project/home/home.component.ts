import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GetUserInvitationsInputDTO } from 'src/app/dto/GetUserInvitationInputDTO';
import { projectInputDTO } from 'src/app/dto/project.input.dto';
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
              private addProjectDialog: MatDialog,
              private dialogCreateProjectService: DialogCreateProjectService,
              private sessionService: sessionManagerService) {
    this.projects = [];
    this.invitations = [];
    this.selectedInvitations = [];
    this.dialogCreateProjectService.onCreateProjectSuccess.subscribe(() => {
      this.closeDialogAddProject();
      this.loadUserProject();
    })
  }

  ngOnInit(): void {
    this.loadUserProject();
    this.loadUserInvitations();
  }

  private loadUserProject(): void {
    this.homeService.getUserProjects().subscribe((projects) => {
      this.projects = [...projects];
    })
  }

  private loadUserInvitations(): void {
    this.homeService.getUserProjectInvitations().subscribe((result) => {
      this.invitations = result;
    });
  }

  public getComponentName(): string {
    return 'HomeComponent';
  }

  public onOpenAddProjectDialog(): void {
    this.addProjectDialog.open(CreateProjectComponent);
  }

  public onClickOnProject(projectId: number): void{
    this.sessionService.setProjectId(projectId);
    this.router.navigateByUrl('/project/backlog/' + projectId);
  }

  public onOpenProjectDetails(project: projectInputDTO): void {
    this.addProjectDialog.open(DialogProjectDetailsComponent, {
      data: {
        project
      }
    });
  }

  public onMoveProjectToDraft(projectId: number): void {

  }

  private closeDialogAddProject(): void {
      this.addProjectDialog.closeAll();
  }

  public selectRow(invitation: GetUserInvitationsInputDTO): void {
    console.log(invitation)
  }

  public onRowSelect(event: Event): void {
    console.log(this.selectedInvitations)
  }

  public onRowUnselect(event: Event): void {

  }

  public onAcceptInvitations(): void {
    let ids:number[] = [];
    this.selectedInvitations.forEach((invitation) => {
      ids.push(invitation.invitationToProjectId);
    })

   this.homeService.acceptInvitationToProject(ids).subscribe(() => {
    this.loadUserProject();
    this.loadUserInvitations();
   });
  }

  public onDeleteInvitations(): void {
    let ids:number[] = [];
      this.selectedInvitations.forEach((invitation) => {
        ids.push(invitation.invitationToProjectId);
      })

    this.homeService.cancelInvitationToProject(ids).subscribe(() => {
      this.loadUserInvitations();
    });
  }

  public isUserAdmin(rowData: projectInputDTO): boolean {
    return rowData.projectManagers.some(manager => this.homeService.sessionManager.getUserId() === manager.id);
  }

}
