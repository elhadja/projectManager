import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { AddUserProject } from "src/app/dto/addUserProject.output";
import { projectInputDTO } from "src/app/dto/project.input.dto";
import { API } from "src/app/services/Api";
import { sessionManagerService } from "src/app/services/sessionManager.service";

@Injectable()
export class HomeService {
    public constructor(private api: API,
                        private sessionManager: sessionManagerService) {

    }

    public getUserProjects(): Observable<projectInputDTO[]> {
        return this.api.get(ApiConstant.USERS_BASE_URI + '/' + this.sessionManager.getUserId() + '/projects');
    }

}