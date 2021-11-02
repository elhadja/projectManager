import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { ApiConstant } from "../common/ApiConstant";
import { AddSprintToProjectOutputDTO } from "../dto/addSprintToProjectOutputDTO";
import { AddUserStoryOutputDTO } from "../dto/addUserStoryOutputDTO";
import { GetUserStoriesInputDTO } from "../dto/getUserStoriesInputDTO";
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
}