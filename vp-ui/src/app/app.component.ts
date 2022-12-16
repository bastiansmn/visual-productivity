import {Component, OnDestroy, OnInit} from '@angular/core';
import {ResponsiveService} from "./services/responsive/responsive.service";
import {AuthService} from "./services/auth/auth.service";
import {AlertService, AlertType} from "./services/alert/alert.service";
import {LoaderService} from "./services/loader/loader.service";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  componentDestroyed$ = new Subject<boolean>();

  constructor(
    private responsiveService: ResponsiveService,
    private authService: AuthService,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) {}

  get loader() {
    return this.loaderService;
  }

  get alert() {
    return this.alertService;
  }

  ngOnInit(): void {
    this.onResize();
    this.authService.validateTokens()
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(isValid => {
        if (!isValid) {
          this.alertService.show(
            "Votre session a expiré, veuillez vous reconnecter",
            { type: AlertType.WARNING, duration: 5000 }
          );
          return;
        }

        this.authService.isLoggedIn.next(true);
        this.authService.getUserInfos()
          .pipe(
            takeUntil(this.componentDestroyed$)
          )
          .subscribe(user => {
            this.authService.loggedUser.next(user);
          });
      });
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  onResize() {
    this.responsiveService.checkWidth();
  }

}
