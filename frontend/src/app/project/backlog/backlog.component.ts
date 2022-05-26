import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { PMConstants } from 'src/app/common/PMConstants';
import { GetSprintsInputDTO } from 'src/app/dto/getSprint.input.dto';
import { GetUserStoriesInputDTO } from 'src/app/dto/getUserStoriesInputDTO';
import { DialogConfirmComponent } from 'src/app/modules/shared/dialog-confirm/dialog-confirm.component';
import { TypeScriptUtil } from 'src/app/modules/shared/typescript.util';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';
import { DialogCreateSprintComponent } from '../dialog/dialog-create-sprint/dialog-create-sprint.component';
import { DialogCreateUerStoryComponent } from '../dialog/dialog-create-uer-story/dialog-create-uer-story.component';
import { BacklogService } from '../services/backlog.service';

interface SprintWrapper {
  sprint: GetSprintsInputDTO,
  selectedUserStoriesFromSprint: GetUserStoriesInputDTO[];
  totalStoryPoints: string,
  totalClosedUserStoriesStoryPoints: string,
  totalOpenedUserStoriesStoryPoints: string
  sprintRangeDates: Array<Date>;
}

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {
  public userStories: GetUserStoriesInputDTO[];
  public readonly projectId: number;
  public backlogTotalStoryPoints: string;
  public sprintWrappers: SprintWrapper[];
  public selectedUserStoriesFromBacklog: GetUserStoriesInputDTO[];
  public isActiveSprint: boolean;

  constructor(private materialDialogservice: MatDialog,
              private backlogService: BacklogService,
              private messageService: MessageService,
              private projectApiService: ProjectApiService,
              private route: ActivatedRoute) { 

    this.projectId = 0;
    const help = this.route.snapshot.paramMap.get('backlog-id');
    if (help != null) {
      this.projectId = +(help);
    } else {
      const id = localStorage.getItem('projectId');
      this.projectId = id != null ? +id : this.projectId;
    }

    this.isActiveSprint = false;
    this.backlogTotalStoryPoints = '0';
    this.userStories = [];
    this.sprintWrappers = [];
    this.selectedUserStoriesFromBacklog = [];
  }

  ngOnInit(): void {
    this.initializeBacklog();
    this.projectApiService.getProjectSprints(this.projectId).subscribe((sprints) => {
      sprints.forEach(sprint => {
        if (sprint.status === PMConstants.SPRINT_STATUS_STARTED) {
          this.isActiveSprint = true;
        }
        this.sprintWrappers.push({
          sprint: sprint,
          selectedUserStoriesFromSprint: [],
          totalStoryPoints: `${this.getTotalStoryPoints(sprint.userStories)}`,
          totalClosedUserStoriesStoryPoints: this.getClosedUserStorytTotalStoryPoints(sprint.userStories),
          totalOpenedUserStoriesStoryPoints: this.getOpenedUserStorytTotalStoryPoints(sprint.userStories),
          sprintRangeDates: [sprint.startDate !== null ? new Date(Date.parse(sprint.startDate)): new Date,
            sprint.endDate !== null ? new Date(Date.parse(sprint.endDate)) : new Date]
        });
      });
    });
  }

  private refresh(): void {
    this.sprintWrappers = [];
    this.ngOnInit();
  }

  private initializeBacklog(): void {
    this.projectApiService.getBacklogUserStories(this.projectId).subscribe((userStories) => {
      this.userStories = [...userStories];
      this.backlogTotalStoryPoints = `${this.getTotalStoryPoints(this.userStories)}`;
    });
  }

  public getTotalStoryPoints(userStories: GetUserStoriesInputDTO[]): number {
    let totalStoryPoint = 0;
    userStories.forEach(us => {
      if (us.storyPoint != null) {
        totalStoryPoint += us.storyPoint;
      }
    });

    return totalStoryPoint;
  }

  public getClosedUserStorytTotalStoryPoints(userStories: GetUserStoriesInputDTO[]): string {
    return `${this.getTotalStoryPoints(userStories.filter(us => us.status === PMConstants.USER_STORY_STATUS_CLOSED))}`;
  }

  public getOpenedUserStorytTotalStoryPoints(userStories: GetUserStoriesInputDTO[]): string {
    return `${this.getTotalStoryPoints(userStories.filter(us => us.status === PMConstants.USER_STORY_STATUS_OPENED))}`;
  }


  public onOpenCreateUserStoryDialogFromBacklog(): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent, {disableClose: true});
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.backlogService.createUserStoryInBacklog(this.projectId, result).subscribe((userSotryId) => {
          result.id = userSotryId;
          if (result.storyPoint != null) {
            this.backlogTotalStoryPoints = (+(this.backlogTotalStoryPoints) + (+result.storyPoint)) + '';
          }
          this.userStories = [...this.userStories, result];
          this.messageService.showSuccessMessage('User story created with success');
        });
      }
    });
  }

  public onOpenCreateUserStoryDialogFromSprint(sprintId: number): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent, {disableClose: true});
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.projectApiService.addUserStoryToSprint(this.projectId, sprintId, result).subscribe((userStoryId) => {
          result.id = userStoryId;
          result.status = PMConstants.USER_STORY_STATUS_OPENED;
          const sprintWrapper = this.sprintWrappers.find(sprintWrapper => sprintWrapper.sprint.id === sprintId);
          if (sprintWrapper != null) {
            sprintWrapper.sprint.userStories = [...sprintWrapper.sprint.userStories, result];
            sprintWrapper.totalStoryPoints =  TypeScriptUtil.toString(this.getTotalStoryPoints(sprintWrapper.sprint.userStories));
            sprintWrapper.totalClosedUserStoriesStoryPoints = this.getClosedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories);
            sprintWrapper.totalOpenedUserStoriesStoryPoints = this.getOpenedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories);
          }
          this.messageService.showSuccessMessage('User story created with success');
        });
      }
    });
  }

  public onOpenCreateSprintDialog(): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateSprintComponent, {disableClose: true});
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.projectApiService.addSprintToProject(this.projectId, result).subscribe((createdSprintId) => {
          result.id = createdSprintId;
          result.status = 'CREATED'; // TODO use constant
          result.userStories = [];
          this.sprintWrappers = [...this.sprintWrappers, {
            sprint: result,
            selectedUserStoriesFromSprint: [],
            totalClosedUserStoriesStoryPoints: '0',
            totalOpenedUserStoriesStoryPoints: '0',
            totalStoryPoints: '0',
            sprintRangeDates: [new Date, new Date]
          }];
          this.messageService.showSuccessMessage('sprint created with success');
        });
      }
    });

  }

  public onOpenUserStory(row: GetUserStoriesInputDTO): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent, {
      data: row,
      width: '600px',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe((userStoryToUpdate) => {
      if (userStoryToUpdate != null) {
        this.projectApiService.updateUserStory(this.projectId, row.id, userStoryToUpdate).subscribe(() => {
          this.refresh();
        });
      }
    });
  }

  public onDeleteSelectedBacklogUserStories(): void {
    this.selectedUserStoriesFromBacklog.forEach(us => {
      this.projectApiService.deleteUserStory(this.projectId, us.id).subscribe(() => {
        this.userStories = this.userStories.filter(usToCheck => usToCheck.id !== us.id);
        this.selectedUserStoriesFromBacklog = this.selectedUserStoriesFromBacklog.filter(usToCheck => usToCheck.id !== us.id);

        if (this.selectedUserStoriesFromBacklog.length === 0) {
          this.messageService.showSuccessMessage('All selected user stories are deleted from backlog');
        }
      });
    });
  }

  public onDeleteSelectedSprintsUserStories(sprintWrapper: SprintWrapper): void {
    sprintWrapper.selectedUserStoriesFromSprint.forEach(us => {
      this.projectApiService.deleteUserStory(this.projectId, us.id).subscribe(() => {
        sprintWrapper.sprint.userStories = sprintWrapper.sprint.userStories.filter((usToCheck: GetUserStoriesInputDTO) => usToCheck.id !== us.id);
        sprintWrapper.selectedUserStoriesFromSprint = sprintWrapper.selectedUserStoriesFromSprint.filter(usToCheck => usToCheck.id !== us.id);

        if (sprintWrapper.selectedUserStoriesFromSprint.length === 0) {
          this.messageService.showSuccessMessage('All selected user stories are deleted from sprint');
        }
      });
    });
  }

  public onStartSprint(sprintWrapper: any): void {
    this.projectApiService.startSprint(this.projectId, sprintWrapper.sprint.id, {
      startDate: sprintWrapper.sprintRangeDates[0],
      endDate: sprintWrapper.sprintRangeDates[1]
    }).subscribe(() => {
      sprintWrapper.sprint.status = 'STARTED';
      this.sprintWrappers = [...this.sprintWrappers];
    });
  }

  public onTerminateSprint(sprintWrapper: SprintWrapper) {
    this.materialDialogservice.open(DialogConfirmComponent).afterClosed().subscribe((accept) => {
      if (accept) {
        this.projectApiService.terminateSprint(this.projectId, sprintWrapper.sprint.id).subscribe(() => {
          sprintWrapper.sprint.status = 'CLOSED';
          this.isActiveSprint = false;
          this.sprintWrappers = [...this.sprintWrappers];
          this.initializeBacklog();
        });
      }
    });
  }

  public onCloseSelectedSprintsUserStories(sprintWrapper: SprintWrapper): void {
    sprintWrapper.selectedUserStoriesFromSprint.forEach(selectedUs => {
      this.projectApiService.closeUserStories(this.projectId, selectedUs.id).subscribe(() => {
        sprintWrapper.selectedUserStoriesFromSprint = sprintWrapper.selectedUserStoriesFromSprint.filter(us => us.id !== selectedUs.id);
        selectedUs.status = PMConstants.USER_STORY_STATUS_CLOSED;
        this.actualizeStorypoints(sprintWrapper);
        this.sprintWrappers = [...this.sprintWrappers];
      });
    });
  }

  public onOpenSelectedSprintsUserStories(sprintWrapper :SprintWrapper): void {
    sprintWrapper.selectedUserStoriesFromSprint.forEach(selectedUs => {
      this.projectApiService.openUserStories(this.projectId, selectedUs.id).subscribe(() => {
        sprintWrapper.selectedUserStoriesFromSprint = sprintWrapper.selectedUserStoriesFromSprint.filter(us => us.id !== selectedUs.id);
        selectedUs.status = PMConstants.USER_STORY_STATUS_OPENED;
        this.actualizeStorypoints(sprintWrapper);
        this.sprintWrappers = [...this.sprintWrappers];
      });
    });
  }

  public dropInBacklog(event: CdkDragDrop<GetUserStoriesInputDTO[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      this.projectApiService.moveUserStoryFromSprintToBacklog(this.projectId, event.previousContainer.data[event.previousIndex].id).subscribe();

      const sourceSprintWrapper = this.sprintWrappers.find(sprintWrapper => {
        return sprintWrapper.sprint.userStories.some(us => us.id === event.previousContainer.data[event.previousIndex].id);
      });

      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);

      if (sourceSprintWrapper != null) {
        this.actualizeStorypoints(sourceSprintWrapper);
      }
      this.backlogTotalStoryPoints = `${this.getTotalStoryPoints(this.userStories)}`;
    }

    this.sprintWrappers = [...this.sprintWrappers];
    this.userStories = [...this.userStories];
  }

  public dropInSprint(event: CdkDragDrop<GetUserStoriesInputDTO[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const sourceSprintWrapper = this.sprintWrappers.find(sprintWrapper => {
        return sprintWrapper.sprint.userStories.some(us => us.id === event.previousContainer.data[event.previousIndex].id);
      });
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
      const targetSprintWrapper = this.sprintWrappers.find(sprintWrapper => {
        return sprintWrapper.sprint.userStories.some(us => us.id === event.container.data[event.currentIndex].id);
      });
      if (targetSprintWrapper != null) {
        this.projectApiService.moveUserStoryToSprint(this.projectId, event.container.data[event.currentIndex].id, targetSprintWrapper.sprint.id).subscribe();
        this.actualizeStorypoints(targetSprintWrapper);
      } 
      if (sourceSprintWrapper != null) {
        this.actualizeStorypoints(sourceSprintWrapper);
      } else {
        this.backlogTotalStoryPoints = `${this.getTotalStoryPoints(this.userStories)}`;
      }
    }
    this.sprintWrappers = [...this.sprintWrappers];
  }

  private actualizeStorypoints(sprintWrapper: SprintWrapper): void {
    sprintWrapper.totalStoryPoints = `${this.getTotalStoryPoints(sprintWrapper.sprint.userStories)}`;
    sprintWrapper.totalClosedUserStoriesStoryPoints = `${this.getClosedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories)}`;
    sprintWrapper.totalOpenedUserStoriesStoryPoints = `${this.getOpenedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories)}`;
  }
  
  get SPRINT_STATUS_STARTED(): string {
    return PMConstants.SPRINT_STATUS_STARTED;
  }

  get now(): Date {
    return new Date();
  }
}
