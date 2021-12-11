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
    this.openTask({projectId: this.projectId});
  }

  public onUpdateTask(task: GetTaskInputDTO): void {
    this.openTask({projectId: this.projectId, task});
  }

  private openTask(popupData: {projectId: number, task?: GetTaskInputDTO}): void {
    this.matDialog.open(DialogCreateTaskComponent, { data: popupData });
  }

  // TODO should be refactored
  public onSprintSelected(): void {
    this.tasksToDisplay = [];
    if (!this.tasksToDisplayBySprint?.has(this.selectedSprint)) {
      this.projectSprints.find(sprint => sprint.id === this.selectedSprint)
                        ?.userStories.forEach(us => {
                          us.tasks.forEach(task => {
                            if (this.tasksToDisplay.every(addedTask => addedTask.id !== task.id)) {
                              if (task.userStoriesIDs == null) {
                                task.userStoriesIDs = [];
                              }
                              task.userStoriesIDs.push(us.id);  // TODO task's userstories should be send by services
                              this.tasksToDisplay.push(task)
                            } else {
                              this.tasksToDisplay.find(t => t.id === task.id)?.userStoriesIDs.push(us.id);
                            }
                          });
                        });
      this.tasksToDisplayBySprint?.set(this.selectedSprint, this.tasksToDisplay);
      this.tasksToDisplay = [...this.tasksToDisplay];
    } else {
      this.tasksToDisplay = [...this.tasksToDisplayBySprint.get(this.selectedSprint) ?? []];
    }

    console.log(this.projectSprints)
  }

  public dodAsArray(dod: string): string[] {
    return dod != null && dod.length > 0 ? dod.split(";") : [];
  }

  // TODO should be refactored by defining a new backend service for more performance
  public onDeleteSelectedTasks(): void {
    // FIXME
    /*
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
    */
  }
}
