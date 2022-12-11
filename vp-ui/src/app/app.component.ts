import {Component, OnDestroy, OnInit} from '@angular/core';
import {ResponsiveService} from "./services/responsive/responsive.service";
import {AuthService} from "./services/auth/auth.service";
import {AlertService} from "./services/alert/alert.service";
import {LoaderService} from "./services/loader/loader.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

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
    this.authService.checkConnection();
  }

  ngOnDestroy(): void {
  }

  onResize() {
    this.responsiveService.checkWidth();
  }

}
