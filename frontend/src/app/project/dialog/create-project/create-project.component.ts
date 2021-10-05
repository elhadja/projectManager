import { Component, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MessageService } from 'src/app/services/message.service';
import { DialogCreateProjectService } from '../../services/dialogCreateProject.service';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {
  projectName: FormControl;
  projectDescription: FormControl;

  constructor(private dialogCreateProjectService: DialogCreateProjectService,
    private messageService: MessageService) {
    this.projectName = new FormControl('', [Validators.required]);
    this.projectDescription = new FormControl('');
  }

  ngOnInit(): void {
  }

  public onAddProject(): void {
    this.dialogCreateProjectService.createProject({
      name: this.projectName.value,
      description: this.projectDescription.value
    }).subscribe(() => {
      this.dialogCreateProjectService.onCreateProjectSuccess.next();
      this.messageService.showSuccessMessage('project created');
    });
  }

  public isValidForm(): boolean {
    return this.projectName.valid;
  }

}
