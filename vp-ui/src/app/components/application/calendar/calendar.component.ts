import {Component} from '@angular/core';
import {CalendarView} from "./calendar-view.enum";
import {BehaviorSubject} from "rxjs";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent {

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
    private datePipe: DatePipe
  ) { }

  handleSwitch($event: { value: string; key: string }) {
    this.currentView$.next(CalendarView[$event.key as keyof typeof CalendarView]);
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
}
