import {Component, OnDestroy, OnInit} from '@angular/core';
import {ResponsiveService} from "./services/responsive/responsive.service";
import {AuthService} from "./services/auth/auth.service";
import {AlertService} from "./services/alert/alert.service";
import {LoaderService} from "./services/loader/loader.service";
import {Subject} from "rxjs";

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
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  onResize() {
    this.responsiveService.checkWidth();
  }

}
