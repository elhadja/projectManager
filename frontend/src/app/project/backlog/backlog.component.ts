import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
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

    this.userStories = [];
    this.backlogTotalStoryPoints = '0'
    this.sprintWrappers = [];
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
          totalStoryPoints: `${this.getTotalStoryPoints(sprint.userStories)}`
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
          result.userStories = [];
          this.sprintWrappers = [...this.sprintWrappers, {
            sprint: result,
            totalClosedUserStoriesStoryPoints: '0',
            totalOpenedUserStoriesStoryPoints: '0',
            totalStoryPoints: '0'
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

}
