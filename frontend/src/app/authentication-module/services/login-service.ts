import { Injectable } from '@angular/core';
import { GoogleLoginProvider, SocialAuthService } from 'angularx-social-login';
import { ApiConstant } from 'src/app/common/ApiConstant';
import { PMConstants } from 'src/app/common/PMConstants';
import { LoginInputDTO } from 'src/app/dto/login.input.interface';
import { LoginOutputDTO } from 'src/app/dto/login.output.interface';
import { UserApiService } from 'src/app/PMApi/user-api.service';
import { API } from 'src/app/services/Api';
import { RoutingService } from 'src/app/services/routing.service';
import { sessionManagerService } from 'src/app/services/sessionManager.service';

@Injectable()
export class LoginService {
  constructor(private api: API,
                private routingService: RoutingService,
                private sessionManager: sessionManagerService,
                private readonly socialAuthService: SocialAuthService,
                private readonly userApiService: UserApiService) {

  }
  public login(output: LoginOutputDTO): void {
    this.api.postWithoutHeaders(ApiConstant.USERS_BASE_URI + '/' + ApiConstant.LOGIN_URI, output)
      .subscribe((response) => {
        this.initializeSession(response);
        this.routingService.gotoProjectComponent();
      });
  }

  private initializeSession(response: LoginInputDTO): void {
    this.sessionManager.start(response.token, response.id);
    this.api.setHttpOptions(response.token);
  }

  public loginWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then((response) => { 
        this.userApiService.loginWithGoogle(response.idToken).subscribe((response) => {
          this.initializeSession(response);
          this.routingService.gotoProjectComponent(); 
        });
      })
      .catch(error => console.log(error));
  }
}