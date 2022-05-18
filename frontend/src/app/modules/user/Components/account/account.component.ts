import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UserDTO } from 'src/app/dto/user.dto';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';
import { DialogUserProfilComponent } from '../user-profile/dialog-user-profil.component';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  public userDTO: UserDTO | undefined;

  constructor(private readonly matDialog: MatDialog,
            private readonly userApiService: UserApiService,
             private readonly sessionManager: sessionManagerService) {
    
  }

  ngOnInit(): void {
    this.userApiService.getUserById(this.sessionManager.getUserId())
      .subscribe((user) => {
        this.userDTO = user;
      });
  }

  public refresh(): void {
    this.userApiService.getUserById(this.sessionManager.getUserId())
      .subscribe((user) => {
        this.userDTO = user;
      });
  }

  public openProfileDialog(): void {
    this.matDialog.open(DialogUserProfilComponent).afterClosed().subscribe(() => this.refresh());
  }
}
