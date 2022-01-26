import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { PMConstants } from "../common/PMConstants";

@Injectable()
export class RouteSecureService implements CanActivate {
    constructor(private router: Router) {
    }
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (localStorage.getItem(PMConstants.SESSION_TOKEN_ID_KEY) == null) {
            this.router.navigate(['/' + PMConstants.AUTHENTICATION_MODULE_BASE_URI, PMConstants.LOGIN_URI]);
        }

        return true;
    }
}