import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetTaskInputDTO } from 'src/app/dto/getTask.input.dto';
import { UserDTO } from 'src/app/dto/user.dto';
import { GetUserStoriesInputDTO } from 'src/app/dto/getUserStoriesInputDTO';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-dialog-create-task',
  templateUrl: './dialog-create-task.component.html',
  styleUrls: ['./dialog-create-task.component.css']
})
export class DialogCreateTaskComponent implements OnInit {
  public taskFormGroup: FormGroup;
  public responsables: UserDTO[];
  public userStories: GetUserStoriesInputDTO[];
  public taskDependencies: GetTaskInputDTO[];
  public sprints: GetSprintsInputDTO[];
  public projectId: number;
  public updateMode: boolean;

  constructor(private readonly fb: FormBuilder,
            private route: ActivatedRoute,
            @Inject(MAT_DIALOG_DATA) public data: {projectId: number, task?: GetTaskInputDTO},
            private readonly projectApiService: ProjectApiService,
            private readonly messageService: MessageService,
            private readonly dialogRef: MatDialogRef<DialogCreateTaskComponent>) {
    this.taskFormGroup = fb.group({
      'description': fb.control('', [Validators.required, Validators.maxLength(255)]),
      'definitionOfDone': fb.array([fb.control('')]),
      'duration': fb.control(null),
      'user': fb.control(null),
      'sprint': fb.control('', [Validators.required]),
      'selectedUserStories': fb.control([], [Validators.required]),
      'dependencies': fb.control([])
    });

    this.updateMode = data.task != null;
    this.projectId = data.projectId;

    this.responsables = [];
    this.userStories = [];
    this.sprints = [];
    this.taskDependencies = [];
   }

  ngOnInit(): void {
    this.projectApiService.getProjectSprints(this.projectId).subscribe(sprints => {
      this.sprints = sprints;
    });

    forkJoin({
      projectSprints: this.projectApiService.getProjectSprints(this.projectId),
      projectUsers: this.projectApiService.getProjectUsers(this.projectId)
    }).subscribe(result => {
      this.sprints = result.projectSprints;
      this.responsables = result.projectUsers;
      if (this.data.task != null) {
        this.initializeForm(this.data.task);
      }
    });
  }

  public onCancel(): void {
    this.closeDialog();
  }

  private closeDialog(): void {
    this.dialogRef.close();
  }

  private initializeForm(task: GetTaskInputDTO): void {
    this.description?.setValue(task.description);
    for (let dod of task.definitionOfDone?.split(';') ?? []) {
      this.definitionOfDone.push(new FormControl(dod));
    }
    if (this.definitionOfDone.length > 1) {
      this.definitionOfDone.removeAt(0);
    }
    this.duration?.setValue(task.duration);
    this.user?.setValue(this.responsables.find(user => user.pseudo === task.userPseudo)?.id);
    this.sprint?.setValue(this.sprints.find(sprint => sprint.userStories.some(us => us.id === task.userStoriesIDs[0]))?.id);
    this.selectedUserStories?.setValue(task.userStoriesIDs);
    this.dependencies?.setValue(task.dependencies.map(dependency => dependency.id));
    this.onSprintSelected();
    this.onUserStorySelected();
  }

  public onSubmit(): void {
    this.updateMode ? this.updateTask() : this.createTask();
  }

  private createTask(): void {
    this.projectApiService.createTask(this.projectId, {
      userId: this.user?.value,
      userStoriesIDs: this.selectedUserStories?.value,
      description: this.description?.value,
      duration: this.duration?.value,
      definitionOfDone: this.ArrayOfStringToString(this.definitionOfDone.value),
      dependenciesIDs: this.dependencies?.value
    }).subscribe(() => {
      this.messageService.showSuccessMessage("task created");
      this.closeDialog();
    });
  }

  private updateTask(): void {
    this.projectApiService.updateTask(this.projectId,
                                     this.data.task?.id as number,
                                     { userId: this.user?.value,
                                      userStoriesIDs: this.selectedUserStories?.value,
                                      description: this.description?.value,
                                      duration: this.duration?.value,
                                      definitionOfDone: this.ArrayOfStringToString(this.definitionOfDone.value),
                                      dependenciesIDs: this.dependencies?.value }).subscribe(() => {
      this.messageService.showSuccessMessage("task modified succesfully");
      this.closeDialog();
    });
  }

  private ArrayOfStringToString(arr: string[]): string {
    let res = "";
    arr.forEach(s => res += (s + ';'));
    if (res.length > 0) {
      res = res.substring(0, res.length-1);
    }
    return res;
  }

  public onSprintSelected(): void {
    const selectedSprint = this.sprints.find(currentSprint => currentSprint.id == this.sprint?.value);
    if (selectedSprint != null) {
      this.userStories = [...selectedSprint.userStories];
    }
  }

  public onUserStorySelected(): void {
    this.taskDependencies = [];
    const selectedUserStories = this.userStories.filter(us => this.selectedUserStories?.value.some((selectedUsId:number) => us.id === selectedUsId));
    if (selectedUserStories != null && selectedUserStories.length > 0) {
      selectedUserStories.forEach(selectedUserStory => {
        selectedUserStory.tasks.forEach(task => {
          if (this.taskDependencies.every(taskDepency => taskDepency.id !== task.id)) {
            this.taskDependencies.push(task);
          }
        });
      });

      this.taskDependencies = [...this.taskDependencies];
    }
  }

  public addDefinitionOfDone(): void {
    this.definitionOfDone.push(new FormControl(''));
  }

  public removeDefinitionOfDone(): void {
    this.definitionOfDone.removeAt(this.definitionOfDone.length - 1);
  }

  get description() {
    return this.taskFormGroup.get('description');
  }

  get duration() {
    return this.taskFormGroup.get('duration');
  }

  get user() {
    return this.taskFormGroup.get('user');
  }

  get selectedUserStories() {
    return this.taskFormGroup.get('selectedUserStories');
  }

  get definitionOfDone() {
    return this.taskFormGroup.get('definitionOfDone') as FormArray;
  }

  get sprint() {
    return this.taskFormGroup.get('sprint');
  }

  get dependencies() {
    return this.taskFormGroup.get('dependencies');
  }
}
