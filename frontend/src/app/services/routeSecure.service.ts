import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { PMConstants } from "../common/PMConstants";
import { RoutingService } from "./routing.service";

@Injectable()
export class RouteSecureService implements CanActivate {
    constructor(private routingService: RoutingService) {
    }
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (localStorage.getItem(PMConstants.SESSION_TOKEN_ID_KEY) == null) {
            this.routingService.gotoLoginComponent();
        }

        return true;
    }
}