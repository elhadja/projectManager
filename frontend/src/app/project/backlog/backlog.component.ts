import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { GetUserStoriesInputDTO } from 'src/app/dto/getUserStoriesInputDTO';
import { ProjectApiService } from 'src/app/PMApi/project.api';
import { MessageService } from 'src/app/services/message.service';
import { DialogCreateSprintComponent } from '../dialog/dialog-create-sprint/dialog-create-sprint.component';
import { DialogCreateUerStoryComponent } from '../dialog/dialog-create-uer-story/dialog-create-uer-story.component';
import { BacklogService } from '../services/backlog.service';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {
  public userStories: GetUserStoriesInputDTO[];
  public sprints: any[];
  public readonly projectId: number;

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
    this.sprints = [];
  }

  ngOnInit(): void {
    this.projectApiService.getBacklogUserStories(this.projectId).subscribe((userStories) => {
      this.userStories = [...userStories];
    });

    this.projectApiService.getProjectSprints(this.projectId).subscribe((sprints) => {
      this.sprints = [...sprints];
    })
  }

  public onOpenCreateUserStoryDialogFromBacklog(): void {
    const dialogRef = this.materialDialogservice.open(DialogCreateUerStoryComponent);
    dialogRef.afterClosed().subscribe((result) => {
      if (result != null) {
        this.backlogService.createUserStoryInBacklog(this.projectId, result).subscribe((userSotryId) => {
          result.id = userSotryId;
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
          const sprint = this.sprints.find(sprint => sprint.id === sprintId);
          sprint.userStories = [...sprint.userStories, result];
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
          this.sprints = [...this.sprints, result];
          this.messageService.showSuccessMessage("sprint created with success");
        })
      }
    });

  }

}
