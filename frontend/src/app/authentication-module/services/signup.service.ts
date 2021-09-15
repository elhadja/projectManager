import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, of } from "rxjs";
import { ApiConstant } from "src/app/common/ApiConstant";
import { PMConstants } from "src/app/common/PMConstants";
import { SignupOutputDTO } from "src/app/dto/signup.output.dto";
import { API } from "src/app/services/Api";

@Injectable()
export class SignupService {
    constructor(private api: API,
                private router: Router) {
        
    }

    public registeruser(output: SignupOutputDTO): void {
        this.api.postWithoutHeaders(ApiConstant.USERS_BASE_URI + '/' + ApiConstant.SIGNUPU, output)
                .subscribe(() => {
                    this.router.navigate([PMConstants.AUTHENTICATION_MODULE_BASE_URI, PMConstants.LOGIN_URI]);
                });
    }
}