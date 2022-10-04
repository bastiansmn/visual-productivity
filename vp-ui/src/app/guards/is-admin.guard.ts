import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanLoad, Route, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from "../services/auth/auth.service";
import {Role} from "../model/role.model";

@Injectable({
  providedIn: 'root'
})
export class IsAdminGuard implements CanActivate, CanLoad {

  constructor(private authService: AuthService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return (this.authService.loggedUser?.isNotLocked
      && this.authService.loggedUser?.isEnabled
      && this.authService.loggedUser?.roles.map((r: Role) => r.name).includes("ROLE_ADMIN")
    ) ?? false;
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return (this.authService.loggedUser?.isNotLocked
      && this.authService.loggedUser?.isEnabled
      && this.authService.loggedUser?.roles.map((r: Role) => r.name).includes("ROLE_ADMIN")
    ) ?? false;
  }
}
