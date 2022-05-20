import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { PMConstants } from '../common/PMConstants';

@Injectable()
export class RoutingService {
  gotoUserProfileComponent() {
    this.router.navigateByUrl('/' + PMConstants.USER_MODULE_BASE_URI + '/' + PMConstants.USER_MODULE_PROFILE);
  }

  gotoAccountComponent() {
    this.router.navigateByUrl('/' + PMConstants.USER_MODULE_BASE_URI + '/' + PMConstants.USER_MODULE_ACCOUNT);
  }

  constructor(private readonly router: Router) {

  }

  public gotoBacklogComponent(projectId: number): void {
    this.router.navigateByUrl('/project/backlog/' + projectId);
  }

  public gotoLoginComponent(): void {
    this.router.navigate(['/' + PMConstants.AUTHENTICATION_MODULE_BASE_URI, PMConstants.LOGIN_URI]);
  }

  public gotoProjectComponent(): void {
    this.router.navigate(['/' + PMConstants.PROJECT_MODULE_BASE_URI]);
  }

  public gotoTaskComponent(projectId: number): void {
    this.router.navigateByUrl('/project/task/' + projectId);
  }

  public gotoSignupComponent(): void {
    this.router.navigateByUrl('/auth/signup');
  }

  public gotoRequestNewPasswordComponent(): void {
    this.router.navigateByUrl('auth/password-request');
  }
}