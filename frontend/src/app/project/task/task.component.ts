import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetTaskInputDTO } from 'src/app/dto/getTask.input.dto';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { DialogCreateTaskComponent } from '../dialog/dialog-create-task/dialog-create-task.component';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  public projectId: number;
  public selectedView: number;
  public selectedSprint: number;
  public projectSprints: GetSprintsInputDTO[];
  public tasksToDisplay: GetTaskInputDTO[];
  public tasksToDisplayBySprint: Map<number, GetTaskInputDTO[]> | undefined;

  constructor(private matDialog: MatDialog,
              private route: ActivatedRoute,
              private projectApiService: ProjectApiService) {
    this.selectedView = 0;
    this.selectedSprint = 0.
    this.projectId = 0;

    this.projectSprints = [];
    this.tasksToDisplay = [];
    this.tasksToDisplayBySprint = undefined;

    const routeParameter = this.route.snapshot.paramMap.get('project-id');
    if (routeParameter != null) {
      this.projectId = +(routeParameter);
    } else {
      const id = localStorage.getItem('projectId');
      this.projectId = id != null ? +id : this.projectId;
    }

  }

  ngOnInit(): void {
    this.projectApiService.getProjectSprints(this.projectId).subscribe(sprints => {
      this.projectSprints = sprints;
    });
  }

  public onCreateTask(): void {
    this.matDialog.open(DialogCreateTaskComponent, { data: {projectId: this.projectId } });
  }

  public onSprintSelected(): void {
    this.tasksToDisplay = [];
    if (!this.tasksToDisplayBySprint?.has(this.selectedSprint)) {
      this.projectSprints.find(sprint => sprint.id === this.selectedSprint)
                        ?.userStories.forEach(us => {
                          us.tasks.forEach(task => {
                            task.userStoryId = us.id;
                            this.tasksToDisplay.push(task)
                          });
                        });
      this.tasksToDisplayBySprint?.set(this.selectedSprint, this.tasksToDisplay);
      this.tasksToDisplay = [...this.tasksToDisplay];
    } else {
      this.tasksToDisplay = [...this.tasksToDisplayBySprint.get(this.selectedSprint) ?? []];
    }
  }

  public dodAsArray(dod: string): string[] {
    return dod != null && dod.length > 0 ? dod.split(";") : [];
  }
}
