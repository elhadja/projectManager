import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GetUserByCriteriaOutputDTO } from 'src/app/dto/GetUserByCriteriaOutputDTO';
import { GetUserInvitationsInputDTO } from 'src/app/dto/GetUserInvitationInputDTO';
import { GetUsersByCriteriaInputDTO } from 'src/app/dto/getUsersByCriteriaInputDTO';
import { projectInputDTO } from 'src/app/dto/project.input.dto';
import { UpdateProjectOutputDTO } from 'src/app/dto/updateProjectOutputDTO';
import { MessageService } from 'src/app/services/message.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { DialogCreateProjectService } from '../../services/dialogCreateProject.service';
import { DialogDetailsProjectService } from '../../services/dialogProjectDetails.service';

interface ProjectUsersTableRow {
  pseudo: string,
  isManager: boolean
}

@Component({
  selector: 'app-dialog-project-details',
  templateUrl: './dialog-project-details.component.html',
  styleUrls: ['./dialog-project-details.component.css']
})
export class DialogProjectDetailsComponent implements OnInit {
  public project: projectInputDTO;
  public projectName: FormControl;
  public projectDescription: FormControl;
  public projectUsersDataTable: ProjectUsersTableRow[];
  public users: GetUsersByCriteriaInputDTO[];
  public selectedUsers: GetUsersByCriteriaInputDTO[];
  public userFirstName: FormControl;
  public userLastName: FormControl;
  public userPseudo: FormControl;
  private removedUsersPseudos: Set<string>;
  private currentManagersPseudos: Set<string>;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogProjectDetailsService: DialogDetailsProjectService,
            private messageServcie: MessageService,
            private sessionManager: sessionManagerService) {
    this.projectName = new FormControl('', [Validators.required, Validators.minLength(2)]),
    this.projectDescription = new FormControl('', [Validators.required, Validators.minLength(2)])
    this.projectUsersDataTable = [];
    this.project = data.project;
    this.selectedUsers = [];
    this.users = [];
    this.removedUsersPseudos = new Set();
    this.currentManagersPseudos = new Set();
    this.userFirstName = new FormControl('');
    this.userLastName = new FormControl('');
    this.userPseudo = new FormControl('');
  }

  ngOnInit(): void {
    this.project.projectManagers.forEach((manager) => this.currentManagersPseudos.add(manager.pseudo));
    this.initProjectInfos();
    this.initTableData();
  }

  private initProjectInfos() {
    this.projectName.setValue(this.project.projectName);
    this.projectDescription.setValue(this.project.projectDescription);
  }

  private initTableData(): void {
    for (let user of this.project.projectUsers) {
      this.projectUsersDataTable.push({
        pseudo: user.pseudo,
        isManager: this.project.projectManagers.some(manager => manager.id === user.id)
      });
    }
    this.projectUsersDataTable = [...this.projectUsersDataTable];
  }

  public getComponentName(): string {
    return 'DialogProjectDetailsComponent';
  }

  public onSendInvitations(): void {
    this.selectedUsers.forEach((user) => {
      this.dialogProjectDetailsService.inviteUsersToProject({
        guestId: user.id,
        authorId: this.sessionManager.getUserId()
      }, this.project.projectId).subscribe(() => {
        this.messageServcie.showSuccessMessage("Invitation sent with success");
      });
    })
  }

  public onSearchUsers(): void {
    this.dialogProjectDetailsService.seachUsers({
      firstname: this.userFirstName.value.length > 0 ? this.userFirstName.value : null,
      lastname: this.userLastName.value.length > 0 ? this.userLastName.value : null,
      pseudo: this.userPseudo.value.length > 0 ? this.userPseudo.value : null,
    }).subscribe((users) => {
      this.selectedUsers = [];
      this.users = [...users];
    })
  }
  
  public onSaveProjectDetails(): void {
    this.synchronizeRealDataToDataTable();

    let managers: number[] = [];
    let users: number[] = [];
    this.project.projectManagers.forEach(manager => managers.push(manager.id));
    this.project.projectUsers.forEach(user => users.push(user.id));

    const output: UpdateProjectOutputDTO = {
      projectName: this.projectName.value,
      projectDescription: this.projectDescription.value,
      projectManagersIds: managers,
      projectUsersIds: users
    }

    this.dialogProjectDetailsService.updateProjectDetails(output, this.project.projectId).subscribe(() => {
      this.messageServcie.showSuccessMessage("Project update successfully");
    });
  }

  private synchronizeRealDataToDataTable(): void {
    this.actualizeManagers();
    this.actulizeProjectUsers();
  }

  public actulizeProjectUsers(): void {
    this.removedUsersPseudos.forEach((userPseudo) => {
      this.project.projectUsers = this.project.projectUsers.filter(user => user.pseudo !== userPseudo);
    });
  }

  private actualizeManagers(): void {
    this.project.projectManagers = this.project.projectManagers.filter(manager => this.currentManagersPseudos.has(manager.pseudo));
  }

  public onDeleteUser(rowData: ProjectUsersTableRow): void {
    this.removedUsersPseudos.add(rowData.pseudo);
    this.currentManagersPseudos.delete(rowData.pseudo);
    this.projectUsersDataTable = this.projectUsersDataTable.filter(row => row.pseudo !== rowData.pseudo);
    this.projectUsersDataTable = [...this.projectUsersDataTable];
  }

  public onUpdateisManager(row: ProjectUsersTableRow): void {
    row.isManager = !row.isManager;
    row.isManager ? this.currentManagersPseudos.add(row.pseudo) : this.currentManagersPseudos.delete(row.pseudo);
  }
}
