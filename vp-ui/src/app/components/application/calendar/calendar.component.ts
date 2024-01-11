import {Component, OnInit} from '@angular/core';
import {CalendarView} from "./calendar-view.enum";
import {BehaviorSubject, take} from "rxjs";
import {DatePipe, TitleCasePipe} from "@angular/common";
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {

  private calendarTitle$ = new BehaviorSubject<string>('');
  get calendarTitle() {
    return this.calendarTitle$.asObservable();
  }

  private currentView$ = new BehaviorSubject<CalendarView>(CalendarView.WEEK);
  calendarViews = Object.entries(CalendarView).map(([key, value]) => ({key, value}));
  CalendarView = CalendarView;
  get currentView() {
    return this.currentView$.asObservable();
  }

  constructor(
    private datePipe: DatePipe,
    private titleCasePipe: TitleCasePipe,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.queryParams
      .pipe(take(1))
      .subscribe(params => {
        const view = params['view'];
        if (!view) return;
        this.currentView$.next(CalendarView[view as keyof typeof CalendarView]);
      });
  }

  handleSwitch($event: { value: string; key: string }) {
    this.currentView$.next(CalendarView[$event.key as keyof typeof CalendarView]);
    const queryParams: Params = { view: $event.key };
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams,
        queryParamsHandling: 'merge', // remove to replace all query params by provided
      }
    );
  }

  getIndexOfDefaultValue() {
    return this.calendarViews.findIndex((value) => value.value === this.currentView$.value);
  }

  changeMonth($event: { firstDate: Date; lastDate: Date }) {
    const month1 = this.datePipe.transform($event.firstDate, 'MMMM');
    if (!month1) return;
    // Capitalize first letter
    const month1Capitalized = month1.charAt(0).toUpperCase() + month1.slice(1);
    const year1 = this.datePipe.transform($event.firstDate, 'yyyy');
    if (!year1) return;

    const month2 = this.datePipe.transform($event.lastDate, 'MMMM');
    if (!month2) return;
    // Capitalize first letter
    const month2Capitalized = month2.charAt(0).toUpperCase() + month2.slice(1);
    const year2 = this.datePipe.transform($event.lastDate, 'yyyy');
    if (!year2) return;

    if (month1 === month2 && year1 === year2) {
      this.calendarTitle$.next(`${month1Capitalized} ${year1}`);
    } else if (year1 === year2) {
      this.calendarTitle$.next(`${month1Capitalized} - ${month2Capitalized} ${year1}`);
    } else {
      this.calendarTitle$.next(`${month1Capitalized} ${year1} - ${month2Capitalized} ${year2}`);
    }
  }

  changeDay($event: Date) {
    this.calendarTitle$.next(
      this.titleCasePipe.transform(
        this.datePipe.transform($event, 'fullDate')!
      )
    );
  }
}
