import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetUserStoriesInputDTO } from 'src/app/dto/getUserStoriesInputDTO';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';

interface LabelValue {
  label: string,
  value: string
}

@Component({
  selector: 'app-dialog-create-task',
  templateUrl: './dialog-create-task.component.html',
  styleUrls: ['./dialog-create-task.component.css']
})
export class DialogCreateTaskComponent implements OnInit {
  public taskFormGroup: FormGroup;
  public responsables: LabelValue[];
  public userStories: GetUserStoriesInputDTO[];
  public sprints: GetSprintsInputDTO[];
  public projectId: number;

  constructor(private readonly fb: FormBuilder,
            private route: ActivatedRoute,
            @Inject(MAT_DIALOG_DATA) public data: {projectId: number},
            private readonly projectApiService: ProjectApiService,
            private readonly messageService: MessageService) {
    this.taskFormGroup = fb.group({
      'description': fb.control('', [Validators.required]),
      'definitionOfDone': fb.array([fb.control('')]),
      'duration': fb.control(null),
      'user': fb.control(null),
      'sprint': fb.control('', [Validators.required]),
      'userStory': fb.control('', [Validators.required]),
    });

    this.projectId = data.projectId;
    this.responsables = [];
    this.userStories = [];
    this.sprints = [];
   }

  ngOnInit(): void {
    this.projectApiService.getProjectSprints(this.projectId).subscribe(sprints => {
      this.sprints = sprints;
    });
  }

  public onSubmit(): void {
    this.projectApiService.createTask(this.projectId, {
      userId: this.user?.value,
      userStoryId: this.userStory?.value,
      description: this.description?.value,
      duration: this.duration?.value,
      definitionOfDone: this.ArrayOfStringToString(this.definitionOfDone.value)
    }).subscribe(() => {
      this.messageService.showSuccessMessage("task created");
    });
  }

  private ArrayOfStringToString(arr: string[]): string {
    let res = "";
    arr.forEach(s => res += (s + ';'));
    return res;
  }

  public onSprintSelected(): void {
    const selectedSprint = this.sprints.find(currentSprint => currentSprint.id == this.sprint?.value);
    if (selectedSprint != null) {
      this.userStories = [...selectedSprint.userStories];
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
}
