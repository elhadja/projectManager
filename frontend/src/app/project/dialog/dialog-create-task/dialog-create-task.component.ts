import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetTaskInputDTO } from 'src/app/dto/getTask.input.dto';
import { GetUsersByCriteriaInputDTO } from 'src/app/dto/getUsersByCriteriaInputDTO';
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
  public responsables: GetUsersByCriteriaInputDTO[];
  public userStories: GetUserStoriesInputDTO[];
  public taskDependencies: GetTaskInputDTO[];
  public sprints: GetSprintsInputDTO[];
  public projectId: number;
  public updateMode: boolean;

  constructor(private readonly fb: FormBuilder,
            private route: ActivatedRoute,
            @Inject(MAT_DIALOG_DATA) public data: {projectId: number, task?: GetTaskInputDTO},
            private readonly projectApiService: ProjectApiService,
            private readonly messageService: MessageService) {
    this.taskFormGroup = fb.group({
      'description': fb.control('', [Validators.required]),
      'definitionOfDone': fb.array([fb.control('')]),
      'duration': fb.control(null),
      'user': fb.control(null),
      'sprint': fb.control('', [Validators.required]),
      'userStory': fb.control(data.task != null ? data.task.userStoryId : '', [Validators.required]),
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
    this.sprint?.setValue(this.sprints.find(sprint => sprint.userStories.some(us => us.id === task.userStoryId))?.id);
    this.userStory?.setValue(task.userStoryId);
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
      userStoryId: this.userStory?.value,
      description: this.description?.value,
      duration: this.duration?.value,
      definitionOfDone: this.ArrayOfStringToString(this.definitionOfDone.value),
      dependenciesIDs: this.dependencies?.value
    }).subscribe(() => {
      this.messageService.showSuccessMessage("task created");
    });
  }

  private updateTask(): void {
    this.projectApiService.updateTask(this.projectId,
                                     this.data.task?.userStoryId as number,
                                     this.data.task?.id as number,
                                     { userId: this.user?.value,
                                      userStoryId: this.userStory?.value,
                                      description: this.description?.value,
                                      duration: this.duration?.value,
                                      definitionOfDone: this.ArrayOfStringToString(this.definitionOfDone.value),
                                      dependenciesIDs: this.dependencies?.value }).subscribe(() => {
      this.messageService.showSuccessMessage("task modified succesfully");
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
    const selectedUserStory = this.userStories.find(us => us.id == this.userStory?.value);
    if (selectedUserStory != null) {
      this.taskDependencies = [...selectedUserStory.tasks];
    }
  }

  public addDefinitionOfDone(): void {
    this.definitionOfDone.push(new FormControl(''));
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

  get userStory() {
    return this.taskFormGroup.get('userStory');
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
