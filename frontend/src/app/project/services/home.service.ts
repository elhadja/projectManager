import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { AddUserProject } from "src/app/dto/addUserProject.output";
import { GetUserInvitationsInputDTO } from "src/app/dto/GetUserInvitationInputDTO";
import { projectInputDTO } from "src/app/dto/project.input.dto";
import { API } from "src/app/services/Api";
import { sessionManagerService } from "src/app/services/sessionManager.service";

@Injectable()
export class HomeService {
    public constructor(private api: API,
                        public sessionManager: sessionManagerService) {

    }

    public getUserProjects(): Observable<projectInputDTO[]> {
        return this.api.get(ApiConstant.USERS_BASE_URI + '/' + this.sessionManager.getUserId() + '/projects');
    }

    public getUserProjectInvitations(): Observable<GetUserInvitationsInputDTO[]> {
        return this.api.get(ApiConstant.USERS_BASE_URI + '/' + this.sessionManager.getUserId() + '/invitationToProjects');
    }

    public acceptInvitationToProject(acceptedInvitations: number[]): Observable<void> {
        return this.api.post(ApiConstant.USERS_BASE_URI + '/' + this.sessionManager.getUserId() + '/projects/' + this.getConcatenedIds(acceptedInvitations), {});
    }

    public cancelInvitationToProject(acceptedInvitations: number[]): Observable<void> {
        return this.api.delete(ApiConstant.USERS_BASE_URI + '/' + this.sessionManager.getUserId() + '/projects/' + this.getConcatenedIds(acceptedInvitations));
    }

    private getConcatenedIds(ids: number[]): string {
        let acceptedProjectsFormat: string = "";
        ids.forEach((projectId, i) => {
            acceptedProjectsFormat += ("" + projectId);
            if (i < ids.length - 1) {
                acceptedProjectsFormat += ","
            }
        })
        return acceptedProjectsFormat;
    }
}