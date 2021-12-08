import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { ApiConstant } from "../common/ApiConstant";
import { AddSprintToProjectOutputDTO } from "../dto/addSprintToProjectOutputDTO";
import { AddUserStoryOutputDTO } from "../dto/addUserStoryOutputDTO";
import { CreateTaskOutputDTO } from "../dto/createTask.output.dto";
import { GetUsersByCriteriaInputDTO } from "../dto/getUsersByCriteriaInputDTO";
import { GetUserStoriesInputDTO } from "../dto/getUserStoriesInputDTO";
import { StartSprintOutputDTO } from "../dto/startSprintOutputDTO";
import { API } from "../services/Api";

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

    public getProjectSprints(projectId: number): Observable<any[]> {
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

    public getProjectUsers(projectId: number): Observable<GetUsersByCriteriaInputDTO[]> {
        return this.api.get(this.baseURI + '/' + projectId + '/users');
    }

    public deleteTasks(projectId: number, userStoryId: number, tasksIDs: number[]): Observable<void> {
        let taskIDsString = "";
        tasksIDs.forEach((id, index) => {
            taskIDsString += `${id}`;
            if (index < tasksIDs.length-1) {
                taskIDsString += ",";
            }
        });
        return this.api.delete(this.baseURI + '/' + projectId + '/user-stories/' + userStoryId + '/tasks/' + taskIDsString);
    }

    public updateTask(projectId:  number, userStoryId: number, taskId: number, input: CreateTaskOutputDTO): Observable<void> {
        return of();
        //return this.api.put(this.baseURI + '/' + projectId + '/user-stories/' + input.userStoryId + '/tasks/' + taskId, input);
    }
}