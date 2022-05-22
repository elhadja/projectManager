import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { RoutingService } from './routing.service';
import { sessionManagerService } from './sessionManager.service';

@Injectable()
export class RouteSecureService implements CanActivate {
  constructor(private routingService: RoutingService, private readonly sessionManagerService: sessionManagerService) {
  }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    if (!this.sessionManagerService.isActive()) {
      this.routingService.gotoLoginComponent();
    }

    return true;
  }
}