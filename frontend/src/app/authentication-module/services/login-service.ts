import { Injectable } from "@angular/core";
import { ApiConstant } from "src/app/common/ApiConstant";
import { PMConstants } from "src/app/common/PMConstants";
import { LoginOutputDTO } from "src/app/dto/login.output.interface";
import { API } from "src/app/services/Api";
import { RoutingService } from "src/app/services/routing.service";
import { sessionManagerService } from "src/app/services/sessionManager.service";

@Injectable()
export class LoginService {
    constructor(private api: API,
                private routingService: RoutingService,
                private sessionManager: sessionManagerService) {

    }
    public login(output: LoginOutputDTO): void {
        this.api.postWithoutHeaders(ApiConstant.USERS_BASE_URI + '/' + ApiConstant.LOGIN_URI, output)
                    .subscribe((response) => {
                        this.sessionManager.setUserid(response.id);
                        localStorage.setItem(PMConstants.SESSION_TOKEN_ID_KEY, response.token);
                        this.api.setHttpOptions(response.token);

                        this.routingService.gotoProjectComponent();
                    });
    }
}