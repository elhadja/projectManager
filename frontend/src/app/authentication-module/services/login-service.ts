import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { ApiConstant } from "src/app/common/ApiConstant";
import { PMConstants } from "src/app/common/PMConstants";
import { LoginOutputDTO } from "src/app/dto/login.output.interface";
import { API } from "src/app/services/Api";

@Injectable()
export class LoginService {
    constructor(private api: API,
                private router: Router) {

    }
    public login(output: LoginOutputDTO): void {
        this.api.postWithoutHeaders(ApiConstant.USERS_BASE_URI + '/' + ApiConstant.LOGIN_URI, output)
                    .subscribe((response) => {
                        localStorage.setItem('token', response.token);
                        this.api.setHttpOptions(response.token);

                        this.router.navigate(["/" + PMConstants.PROJECT_MODULE_BASE_URI]);
                    });
    }
}