import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { PMConstants } from 'src/app/common/PMConstants';
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
  // kanban
  public todoTasks: GetTaskInputDTO[];
  public doingTasks: GetTaskInputDTO[];
  public doneTasks: GetTaskInputDTO[];

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
    this.todoTasks = [];
    this.doingTasks = [];
    this.doneTasks = [];
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
        this.selectedSprint = sprints[0].id; // TODO should be the active sprint
        this.initializeKanban(sprints);
        this.onSprintSelected();
      }
    });
  }

  private initializeKanban(sprints: GetSprintsInputDTO[]): void {
    sprints[0].userStories.forEach(us => us.tasks.forEach(task => { // TOTO should be the active sprint
      if (task.status === PMConstants.TASK_STATUS_TODO && this.todoTasks.every(kanbanTask => kanbanTask.id !== task.id)) {
        this.todoTasks.push(task);
      } else if (task.status === PMConstants.TASK_STATUS_DOING && this.doingTasks.every(kanbanTask => kanbanTask.id !== task.id)) {
        this.doingTasks.push(task);
      } else if (task.status === PMConstants.TASK_STATUS_DONE && this.doneTasks.every(kanbanTask => kanbanTask.id !== task.id)) {
        this.doneTasks.push(task);
      }
    }));
  }

  public dropTaskOnDoingList(event: CdkDragDrop<GetTaskInputDTO[]>): void {
    event.previousContainer.data[event.previousIndex].status = PMConstants.TASK_STATUS_DOING;
    this.projectApiService.setTaskStatus(this.projectId, event.previousContainer.data[event.previousIndex].id, JSON.stringify(PMConstants.TASK_STATUS_DOING)).subscribe();
    this.dropDraggableItem(event);
  }

  public dropTaskOnDoneList(event: CdkDragDrop<GetTaskInputDTO[]>): void {
    event.previousContainer.data[event.previousIndex].status = PMConstants.TASK_STATUS_DONE;
    this.projectApiService.setTaskStatus(this.projectId, event.previousContainer.data[event.previousIndex].id, JSON.stringify(PMConstants.TASK_STATUS_DONE)).subscribe();
    this.dropDraggableItem(event);
  }

  public dropTaskOnTodoList(event: CdkDragDrop<GetTaskInputDTO[]>): void {
    event.previousContainer.data[event.previousIndex].status = PMConstants.TASK_STATUS_TODO;
    this.projectApiService.setTaskStatus(this.projectId, event.previousContainer.data[event.previousIndex].id, JSON.stringify(PMConstants.TASK_STATUS_TODO)).subscribe();
    this.dropDraggableItem(event);
  }

  private dropDraggableItem(event: CdkDragDrop<GetTaskInputDTO[]>): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
    }
  }

  public onCreateTask(): void {
    this.openTask({projectId: this.projectId});
  }

  public onUpdateTask(task: GetTaskInputDTO): void {
    this.openTask({projectId: this.projectId, task});
  }

  private openTask(popupData: {projectId: number, task?: GetTaskInputDTO}): void {
    const dialogRef = this.matDialog.open(DialogCreateTaskComponent, { data: popupData, disableClose: true });
    dialogRef.afterClosed().subscribe(() => {
      this.ngOnInit(); //TODO should be optimized by getting only tasks related to the current sprint
    });
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

  public onDeleteSelectedTasks(): void {
    const taskIDs = this.selectedTasks.map(task => task.id);
    if (taskIDs != null) {
      this.projectApiService.deleteTasks(this.projectId, taskIDs).subscribe(() => {
        this.tasksToDisplay = [...this.tasksToDisplay.filter(task => taskIDs.every(deletedTaskId => deletedTaskId !==task.id))];
        this.selectedTasks = [];
        this.messageService.showSuccessMessage("toutes les tâches ont été supprimées");
      });
    }
  }
}
