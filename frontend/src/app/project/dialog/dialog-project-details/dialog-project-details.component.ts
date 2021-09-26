import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { GetUserByCriteriaOutputDTO } from 'src/app/dto/GetUserByCriteriaOutputDTO';
import { GetUserInvitationsInputDTO } from 'src/app/dto/GetUserInvitationInputDTO';
import { GetUsersByCriteriaInputDTO } from 'src/app/dto/getUsersByCriteriaInputDTO';
import { projectInputDTO } from 'src/app/dto/project.input.dto';
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

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
            private dialogProjectDetailsService: DialogDetailsProjectService) {
    this.projectName = new FormControl('', [Validators.required, Validators.minLength(2)]),
    this.projectDescription = new FormControl('', [Validators.required, Validators.minLength(2)])
    this.projectUsersDataTable = [];
    this.project = data.project;
    this.selectedUsers = [];
    this.users = [];
    this.userFirstName = new FormControl('');
    this.userLastName = new FormControl('');
    this.userPseudo = new FormControl('');
  }

  ngOnInit(): void {
    this.initProjectInfos();
    this.initTableData();
  }

  private initProjectInfos() {
    this.projectName.setValue(this.project.projectName);
    this.projectDescription.setValue(this.project.projectDescription);
  }

  private initTableData(): void {
    let help: ProjectUsersTableRow[] = [];
    for (let manager of this.project.projectManagers) {
      help.push({
        pseudo: manager.pseudo,
        isManager: true
      });
    }

    for (let user of this.project.projectUsers) {
      // TODO backend: don't repeat infos
      help.push({
        pseudo: user.pseudo,
        isManager: false
      });
    }
    this.projectUsersDataTable = [...help];
  }

  public getComponentName(): string {
    return 'DialogProjectDetailsComponent';
  }

  public onSendInvitations(): void {
    this.selectedUsers.forEach((user) => {
      this.dialogProjectDetailsService.inviteUsersToProject({
        guestId: user.id,
        authorId: 4
      }, this.project.projectId).subscribe();
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
}
