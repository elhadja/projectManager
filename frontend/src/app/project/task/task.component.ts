import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetTaskInputDTO } from 'src/app/dto/getTask.input.dto';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';
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
  public selectedTasks: GetTaskInputDTO[];

  constructor(private matDialog: MatDialog,
              private route: ActivatedRoute,
              private projectApiService: ProjectApiService,
              private messageService: MessageService) {
    this.selectedView = 0;
    this.selectedSprint = 0.
    this.projectId = 0;

    this.projectSprints = [];
    this.tasksToDisplay = [];
    this.selectedTasks = [];
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
      if (sprints != null && sprints.length > 0) {
        this.selectedSprint = sprints[0].id;
        this.onSprintSelected();
      }
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

  // TODO should be refactored by defining a new backend service for more performance
  public onDeleteSelectedTasks(): void {
    let tasksToDeleteByUserStoryId: Map<number, number[]>;
    tasksToDeleteByUserStoryId = new Map();
    this.selectedTasks.forEach(task => {
      if (!tasksToDeleteByUserStoryId.has(task.userStoryId)) {
        tasksToDeleteByUserStoryId.set(task.userStoryId, []);
      } 
      if (task.id != null) {
        tasksToDeleteByUserStoryId.get(task.userStoryId)?.push(task.id);
      }
    });

    for (let usId of tasksToDeleteByUserStoryId.keys()) {
      const taskstoDelete = tasksToDeleteByUserStoryId.get(usId);
      if (taskstoDelete != null) {
        this.projectApiService.deleteTasks(this.projectId, usId, taskstoDelete).subscribe(() => {
          this.tasksToDisplay = [...this.tasksToDisplay.filter(taskToDisplay => taskstoDelete.every(deletedTaskId => deletedTaskId !== taskToDisplay.id))];
          this.selectedTasks = [...this.selectedTasks.filter(selectedTask => taskstoDelete.every(deletedTaskId => deletedTaskId !== selectedTask.id))];
          this.messageService.showSuccessMessage("task for U.S " + usId + 'deleted succesfully');
        });
      }
    }
  }
}
