import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { PMConstants } from 'src/app/common/PMConstants';
import { GetUserStoriesInputDTO } from 'src/app/dto/getUserStoriesInputDTO';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';
import { DialogCreateSprintComponent } from '../dialog/dialog-create-sprint/dialog-create-sprint.component';
import { DialogCreateUerStoryComponent } from '../dialog/dialog-create-uer-story/dialog-create-uer-story.component';
import { DialogUserStoryDetailsComponent } from '../dialog/dialog-user-story-details/dialog-user-story-details.component';
import { BacklogService } from '../services/backlog.service';

interface SprintWrapper {
  sprint: any,
  totalStoryPoints: string,
  totalClosedUserStoriesStoryPoints: string,
  totalOpenedUserStoriesStoryPoints: string
  sprintRangeDates: Array<Date|null>;
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
  public selectedUserStoriesFromSprint: GetUserStoriesInputDTO[];
  public selectedUserStoriesFromBacklog: GetUserStoriesInputDTO[];

  constructor(private materialDialogservice: MatDialog,
              private backlogService: BacklogService,
              private messageService: MessageService,
              private projectApiService: ProjectApiService,
              private route: ActivatedRoute) { 

    this.projectId = 0;
    const help = this.route.snapshot.paramMap.get('backlog-id');
    if (help != null) {
      this.projectId = +(help);
    }

    this.backlogTotalStoryPoints = '0'
    this.userStories = [];
    this.sprintWrappers = [];
    this.selectedUserStoriesFromSprint = [];
    this.selectedUserStoriesFromBacklog = [];
  }

  ngOnInit(): void {
    this.projectApiService.getBacklogUserStories(this.projectId).subscribe((userStories) => {
      this.userStories = [...userStories];
      this.backlogTotalStoryPoints = `${this.getTotalStoryPoints(this.userStories)}`;
    });

    this.projectApiService.getProjectSprints(this.projectId).subscribe((sprints) => {
      sprints.forEach(sprint => {
        this.sprintWrappers.push({
          sprint: sprint,
          totalClosedUserStoriesStoryPoints: this.getClosedUserStorytTotalStoryPoints(sprint.userStories),
          totalOpenedUserStoriesStoryPoints: this.getOpenedUserStorytTotalStoryPoints(sprint.userStories),
          totalStoryPoints: `${this.getTotalStoryPoints(sprint.userStories)}`,
          sprintRangeDates: [sprint.startDate !== null ? new Date(Date.parse(sprint.startDate)): null,
                             sprint.endDate !== null ? new Date(Date.parse(sprint.endDate)) : null]
        });
      });
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

  // TODO put this mehtod on global service
  public toString(num: number): string {
    return `${num}`;
  }

  public getClosedUserStorytTotalStoryPoints(userStories: GetUserStoriesInputDTO[]): string {
      return `${this.getTotalStoryPoints(userStories.filter(us => us.status === 'CLOSED'))}`;
  }

  public getOpenedUserStorytTotalStoryPoints(userStories: GetUserStoriesInputDTO[]): string {
      return `${this.getTotalStoryPoints(userStories.filter(us => us.status === 'OPEN'))}`;
  }


  public onOpenCreateUserStoryDialogFromBacklog(): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.backlogService.createUserStoryInBacklog(this.projectId, result).subscribe((userSotryId) => {
          result.id = userSotryId;
          if (result.storyPoint != null) {
            this.backlogTotalStoryPoints = (+(this.backlogTotalStoryPoints) + (+result.storyPoint)) + '';
          }
          this.userStories = [...this.userStories, result];
          this.messageService.showSuccessMessage("User story created with success");
        })
      }
    });
  }

  public onOpenCreateUserStoryDialogFromSprint(sprintId: number): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.projectApiService.addUserStoryToSprint(this.projectId, sprintId, result).subscribe((userStoryId) => {
          result.id = userStoryId;
          result.status = PMConstants.USER_STORY_STATUS_OPENED;
          const sprintWrapper = this.sprintWrappers.find(sprintWrapper => sprintWrapper.sprint.id === sprintId);
          if (sprintWrapper != null) {
            sprintWrapper.sprint.userStories = [...sprintWrapper.sprint.userStories, result];
            sprintWrapper.totalStoryPoints =  this.toString(this.getTotalStoryPoints(sprintWrapper.sprint.userStories));
            sprintWrapper.totalClosedUserStoriesStoryPoints = this.getClosedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories);
            sprintWrapper.totalOpenedUserStoriesStoryPoints = this.getOpenedUserStorytTotalStoryPoints(sprintWrapper.sprint.userStories);
          }
          this.messageService.showSuccessMessage("User story created with success");
        })
      }
    });
  }

  public onOpenCreateSprintDialog(): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateSprintComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.projectApiService.addSprintToProject(this.projectId, result).subscribe((createdSprintId) => {
          result.id = createdSprintId;
          result.status = 'CREATED';
          result.userStories = [];
          this.sprintWrappers = [...this.sprintWrappers, {
            sprint: result,
            totalClosedUserStoriesStoryPoints: '0',
            totalOpenedUserStoriesStoryPoints: '0',
            totalStoryPoints: '0',
            sprintRangeDates: []
          }];
          this.messageService.showSuccessMessage("sprint created with success");
        })
      }
    });

  }

  public onOpenUserStory(row: GetUserStoriesInputDTO): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent, {
      data: row
    });

    dialogRef.afterClosed().subscribe((userStoryToUpdate) => {
      if (userStoryToUpdate != null) {
        this.projectApiService.updateUserStory(this.projectId, row.id, userStoryToUpdate).subscribe(() => {
          this.ngOnInit();
        });
      }
    });
  }

  public onDeleteSelectedBacklogUserStories(): void {
    this.selectedUserStoriesFromBacklog.forEach(us => {
      this.projectApiService.deleteUserStory(this.projectId, us.id).subscribe(() => {
        this.userStories = this.userStories.filter(usToCheck => usToCheck.id !== us.id);
        this.selectedUserStoriesFromBacklog = this.selectedUserStoriesFromBacklog.filter(usToCheck => usToCheck.id !== us.id)

        if (this.selectedUserStoriesFromBacklog.length === 0) {
          this.messageService.showSuccessMessage("All selected user stories are deleted from backlog");
        }
      });
    });
  }

  public onDeleteSelectedSprintsUserStories(sprintWrapper: SprintWrapper): void {
    this.selectedUserStoriesFromSprint.forEach(us => {
      this.projectApiService.deleteUserStory(this.projectId, us.id).subscribe(() => {
        sprintWrapper.sprint.userStories = sprintWrapper.sprint.userStories.filter((usToCheck: GetUserStoriesInputDTO) => usToCheck.id !== us.id);
        this.selectedUserStoriesFromSprint = this.selectedUserStoriesFromSprint.filter(usToCheck => usToCheck.id !== us.id)

        if (this.selectedUserStoriesFromSprint.length === 0) {
          this.messageService.showSuccessMessage("All selected user stories are deleted from sprint");
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
      this.sprintWrappers = [...this.sprintWrappers]
    });
  }

  public onTerminateSprint(sprintWrapper: SprintWrapper) {
    this.projectApiService.terminateSprint(this.projectId, sprintWrapper.sprint.id).subscribe(() => {
      sprintWrapper.sprint.status = 'CLOSED';
      this.sprintWrappers = [...this.sprintWrappers];
    });
  }

  public onCloseSelectedSprintsUserStories(): void {
    this.selectedUserStoriesFromSprint.forEach(selectedUs => {
      this.projectApiService.closeUserStories(this.projectId, selectedUs.id).subscribe(() => {
        this.selectedUserStoriesFromSprint = this.selectedUserStoriesFromSprint.filter(us => us.id !== selectedUs.id);
        selectedUs.status = PMConstants.USER_STORY_STATUS_CLOSED;
        this.sprintWrappers = [...this.sprintWrappers];
      });
    })
  }

  public onOpenSelectedSprintsUserStories(): void {
    this.selectedUserStoriesFromSprint.forEach(selectedUs => {
      this.projectApiService.openUserStories(this.projectId, selectedUs.id).subscribe(() => {
        this.selectedUserStoriesFromSprint = this.selectedUserStoriesFromSprint.filter(us => us.id !== selectedUs.id);
        selectedUs.status = PMConstants.USER_STORY_STATUS_OPENED;
        this.sprintWrappers = [...this.sprintWrappers];
      });
    })
  }

}
