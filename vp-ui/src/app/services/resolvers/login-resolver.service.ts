import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class LoginResolverService implements Resolve<any>{

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {

    return this.authService.initAuthFlow()
      .then(logged => {
        console.log("Logged in:", logged);
      }).catch(() => {
        this.router.navigate(["/discover"])
        console.warn("WARN: Cannot log in.");
      });
  }
}
