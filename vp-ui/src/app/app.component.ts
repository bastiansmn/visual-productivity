import {Component, OnDestroy, OnInit} from '@angular/core';
import {ResponsiveService} from "./services/responsive.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {

  responsiveSubscription!: Subscription;
  isMobile: boolean = false;

  constructor(
    private responsiveService: ResponsiveService
  ) {}

  ngOnInit(): void {
    this.responsiveSubscription = this.responsiveService.getMobileStatus().subscribe(isMobile => {
      this.isMobile = isMobile;
    });
    this.onResize();
  }

  ngOnDestroy(): void {
    this.responsiveSubscription.unsubscribe();
  }

  onResize() {
    this.responsiveService.checkWidth();
  }

}
