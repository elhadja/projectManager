import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AddSprintToProjectOutputDTO } from '../dto/addSprintToProjectOutputDTO';
import { AddUserStoryOutputDTO } from '../dto/addUserStoryOutputDTO';
import { CreateTaskOutputDTO } from '../dto/createTask.output.dto';
import { GetSprintsInputDTO } from '../dto/getSprint.input.dto';
import { UserDTO } from '../dto/user.dto';
import { GetUserStoriesInputDTO } from '../dto/getUserStoriesInputDTO';
import { StartSprintOutputDTO } from '../dto/startSprintOutputDTO';
import { API } from '../services/Api';
import { CustomRevisionEntityDTO } from '../dto/custom-revision-entity.dto';

@Injectable()
export class ProjectApiService {
  private readonly baseURI = '/projects';
  private readonly backlog = 'backlog';
  private readonly userStories = 'user-stories'; 
  private readonly sprint = 'sprints';
  constructor(private api: API) {
        
  }

  public getBacklogUserStories(projectId: number): Observable<GetUserStoriesInputDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/' + this.backlog + '/' + this.userStories);
  }

  public addSprintToProject(projectId: number, input: AddSprintToProjectOutputDTO): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/' + this.sprint, input);
  }

  public getProjectSprints(projectId: number): Observable<GetSprintsInputDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/' + this.sprint);
  }

  public addUserStoryToSprint(projectId: number, sprintId: number, input: AddUserStoryOutputDTO): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/' + this.sprint + '/' + sprintId + '/' + this.userStories, input);
  }

  public updateUserStory(projectId: number, userStoryId: number, input: AddUserStoryOutputDTO): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/user-stories/' + userStoryId, input);
  }

  public deleteUserStory(projectId: number, userStoryId: number): Observable<void> {
    return this.api.delete(this.baseURI + '/' + projectId + '/user-stories/' + userStoryId);
  }

  public deleteSprint(projectId: number, sprintId: number): Observable<void> {
    return this.api.delete(this.baseURI + '/' + projectId + '/sprints/' + sprintId);
  }

  public startSprint(projectId: number, sprintId: number, input: StartSprintOutputDTO): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/sprints/' + sprintId + '/start', input);
  }

  public terminateSprint(projectId: number, sprintId: number): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/sprints/' + sprintId + '/terminate', null);
  }

  public closeUserStories(projectId: number, userStoryId: number): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/user-stories/' + userStoryId + '/close', null);
  }

  public openUserStories(projectId: number, userStoryId: number): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/user-stories/' + userStoryId + '/open', null);
  }

  public createTask(projectId: number, input: CreateTaskOutputDTO): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/tasks', input);
  }

  public getProjectUsers(projectId: number): Observable<UserDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/users');
  }

  public deleteTasks(projectId: number, tasksIDs:number[]): Observable<void> {
    let taskIDsString = '';
    tasksIDs.forEach((id, index) => {
      taskIDsString += `${id}`;
      if (index < tasksIDs.length-1) {
        taskIDsString += ',';
      }
    });

    return this.api.delete(this.baseURI + '/' + projectId + '/tasks/' + taskIDsString);
  }

  public updateTask(projectId:  number, taskId: number, input: CreateTaskOutputDTO): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/tasks/' + taskId, input);
  }

  public setTaskStatus(projectId: number, taskId: number, status: string): Observable<void> {
    return this.api.put(this.baseURI + '/' + projectId + '/tasks/' + taskId + '/setStatus', status);
  }

  public moveUserStoryFromSprintToBacklog(projectId: number, userStoryid: number): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/backlog/user-stories/' + userStoryid, null);
  }

  public moveUserStoryToSprint(projectId: number, userStoryid: number, sprintId: number): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/sprints/' + sprintId + '/user-stories/' + userStoryid, null);
  }

  public removeUserFromProject(projectId: number, userId: number): Observable<void> {
    return this.api.post(this.baseURI + '/' + projectId + '/users/' + userId, null);
  }

  public getUserStoryActivities(projectId: number, usId: number): Observable<CustomRevisionEntityDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/user-stories/' + usId + '/activities');
  }
  public getTaskActivities(projectId: number, taskId: number): Observable<CustomRevisionEntityDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/tasks/' + taskId + '/activities');
  }

  public getSprintActivities(projectId: number, sprintId: number): Observable<CustomRevisionEntityDTO[]> {
    return this.api.get(this.baseURI + '/' + projectId + '/sprints/' + sprintId + '/activities');
  }
}