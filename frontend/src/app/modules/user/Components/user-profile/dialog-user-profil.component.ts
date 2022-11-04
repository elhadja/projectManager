import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { MessageService } from 'src/app/services/message.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './dialog-user-profil.component.html',
  styleUrls: []
})
export class DialogUserProfilComponent implements OnInit {
  public userProfileForm: FormGroup;

  constructor(private readonly dialogRef: MatDialogRef<DialogUserProfilComponent>,
              private fb: FormBuilder,
              private userApiService: UserApiService,
              private readonly sessionManagerService: sessionManagerService,
              private readonly messageService: MessageService) { 
    this.userProfileForm = fb.group(
      {
        firstname: fb.control('', [Validators.minLength(2)]),
        lastname: fb.control('', [Validators.minLength(2)]),
        pseudo: fb.control('', [Validators.minLength(2), Validators.required]),
      }
    );
  }

  ngOnInit(): void {
    this.userApiService.getUserById(this.sessionManagerService.getUserId()).subscribe(
      (user) => {
        this.firstname?.setValue(user.firstname);
        this.lastname?.setValue(user.lastname);
        this.pseudo?.setValue(user.pseudo);
      }
    );
  }

  public updateUserProfile(): void {
    console.log(this.userProfileForm.get('firstname')?.value);
    this.userApiService.updateUser(
      {
        firstname: this.firstname?.value,
        lastname: this.lastname?.value,
        pseudo: this.pseudo?.value
      }, this.sessionManagerService.getUserId()).subscribe(() => {
      this.messageService.showSuccessMessage('User updated.');
      this.dialogRef.close();
    });
  }

  get firstname() {
    return this.userProfileForm.get('firstname');
  }

  get lastname() {
    return this.userProfileForm.get('lastname');
  }

  get pseudo() {
    return this.userProfileForm.get('pseudo');
  }

  get componentName(): string {
    return  'DialogUserProfilComponent';
  }
}
