import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanLoad, Route, RouterStateSnapshot, UrlSegment, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from "../services/auth/auth.service";
import {User} from "../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class IsLoggedInGuard implements CanActivate, CanLoad {

  constructor(private authService: AuthService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const stringUser = sessionStorage.getItem(this.authService.SAVE_FIELD) ?? localStorage.getItem(this.authService.SAVE_FIELD);

    if (!stringUser) return false;

    const user: User = JSON.parse(stringUser);

    if (user.isEnabled && user.isNotLocked) return this.authService.isLoggedIn;
    return false;
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const stringUser = sessionStorage.getItem(this.authService.SAVE_FIELD) ?? localStorage.getItem(this.authService.SAVE_FIELD);

    if (!stringUser) return false;

    const user: User = JSON.parse(stringUser);

    if (user.isEnabled && user.isNotLocked) return this.authService.isLoggedIn;
    return false;
  }
}
