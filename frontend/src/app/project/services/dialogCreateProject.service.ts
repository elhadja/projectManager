import { Injectable } from "@angular/core"
import { Observable, Subject } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { AddUserProject } from "src/app/dto/addUserProject.output";
import { API } from "src/app/services/Api";
import { sessionManagerService } from "src/app/services/sessionManager.service";

@Injectable()
export class DialogCreateProjectService {
    public onCreateProjectSuccess: Subject<void>;

    public constructor(private api: API, private sessionManagerService: sessionManagerService) {
        this.onCreateProjectSuccess = new Subject<void>();
    }

    public createProject(input: AddUserProject): Observable<void> {
        return this.api.post(ApiConstant.USERS_BASE_URI + '/' + this.sessionManagerService.getUserId() + '/projects', input);
    }
}
