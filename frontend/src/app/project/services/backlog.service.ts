import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { AddUserStoryOutputDTO } from "src/app/dto/addUserStoryOutputDTO";
import { API } from "src/app/services/Api";

@Injectable()
export class BacklogService {
    constructor(private api: API) {

    }

    public createUserStoryInBacklog(projectId: number, input: AddUserStoryOutputDTO): Observable<void> {
        return this.api.post(ApiConstant.PROJECTS_BASE_URI + '/' + projectId + '/' + ApiConstant.BACKLOG + '/' + 'user-stories', input);
    }
}