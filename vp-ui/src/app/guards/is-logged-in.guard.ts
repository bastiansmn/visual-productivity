import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable, take} from 'rxjs';
import {AuthService} from "../services/auth/auth.service";
import {CookieService} from "ngx-cookie-service";
import {LoaderService} from "../services/loader/loader.service";
import {AlertService, AlertType} from "../services/alert/alert.service";

@Injectable({
  providedIn: 'root'
})
export class IsLoggedInGuard  {

  constructor(
    private authService: AuthService,
    private router: Router,
    private cookieService: CookieService,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) {}

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    return this.canActivate(childRoute, state);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authService.isLoggedIn.getValue()) return true;
    this.loaderService.show();

    const tokenValidation = this.authService.validateTokens();

    tokenValidation.subscribe(isValid => {
      if (!isValid) {
        this.router.navigate(['/login']);
        this.cookieService.deleteAll();
        this.authService.isLoggedIn.next(false);
        this.loaderService.hide();
        this.alertService.show(
          'Votre session a expiré, veuillez vous reconnecter.',
          { type: AlertType.WARNING, duration: 5000 }
        );
        return;
      }

      this.authService.isLoggedIn.next(true);
      this.authService.getUserInfos()
        .pipe(take(1))
        .subscribe(user => {
          if (!user) {
            this.router.navigate(['/login']);
            this.cookieService.deleteAll();
            this.authService.isLoggedIn.next(false);
            this.loaderService.hide();
            this.alertService.show(
              "Impossible de récupérer vos informations, veuillez vous reconnecter.",
              { type: AlertType.WARNING, duration: 5000 }
            );
            return;
          }

          this.loaderService.hide();
          this.authService.loggedUser.next(user);
        });
    })

    return tokenValidation;
  }

}
