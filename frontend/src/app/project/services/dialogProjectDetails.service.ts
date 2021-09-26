import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { GetUserByCriteriaOutputDTO } from "src/app/dto/GetUserByCriteriaOutputDTO";
import { API } from "src/app/services/Api";

@Injectable()
export class DialogDetailsProjectService {
    constructor(private api: API) {

    }

    public seachUsers(input: GetUserByCriteriaOutputDTO): Observable<any[]> {
        return this.api.post(ApiConstant.USERS_BASE_URI, input);
    }

    // TODO use DTO
    public inviteUsersToProject(input: any, idProject: number): Observable<void> {
        return this.api.post(ApiConstant.PROJECTS_BASE_URI + '/' + idProject + '/inviteUsers', input);
    }
}