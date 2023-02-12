import {AfterViewInit, Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {BehaviorSubject, Subject, takeUntil, timer} from "rxjs";

@Component({
  selector: 'app-week-view',
  templateUrl: './week-view.component.html',
  styleUrls: ['./week-view.component.scss']
})
export class WeekViewComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild("calendarWrapper") calendarWrapper!: ElementRef<HTMLDivElement>
  @ViewChild("timeCursor") timeCursor!: ElementRef<HTMLDivElement>

  @Output() weekChanged = new EventEmitter<{
    firstDate: Date,
    lastDate: Date
  }>();

  private componentDestroyed$ = new Subject<boolean>();

  days!: {name: string, date: Date, current: boolean}[];

  hours = [ "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45" ];
  // Number of subdivisions per hour
  readonly HOURS_SUBDIVISION = 4;

  prevWeek() {
    // Get the days of the previous week
    this.days = this.getDaysOfWeek(new Date(this.days[0].date.getTime() - 24 * 60 * 60 * 1000));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  nextWeek() {
    // Get the days of the next week
    this.days = this.getDaysOfWeek(new Date(this.days[6].date.getTime() + 24 * 60 * 60 * 1000));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  ngOnInit(): void {
    // Get the days of the current week
    this.days = this.getDaysOfWeek();
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  private getDaysOfWeek(anyDayOfWeek?: Date) {
    const days: {name: string, date: Date, current: boolean}[] = [];
    if (!anyDayOfWeek) anyDayOfWeek = new Date();
    const day = anyDayOfWeek.getDay();
    const diff = anyDayOfWeek.getDate() - day + (day === 0 ? -6 : 1);
    for (let i = 0; i < 7; i++) {
      const day = new Date(anyDayOfWeek.setDate(diff + i));
      // Check if day and new Date() are same day
      const current = day.getDate() === new Date().getDate() && day.getMonth() === new Date().getMonth() && day.getFullYear() === new Date().getFullYear();
      days.push({name: this.getDayName(day.getDay()), date: day, current});
    }
    return days;
  }

  private placeTimeCursor() {
    const currentHour = new Date().getHours();
    const currentMinute = new Date().getMinutes();
    const dayPassedPercentage = (currentHour * 60 + currentMinute) / (24*60);

    // Get the height of a tr
    const trHeight = this.calendarWrapper.nativeElement.querySelector("tr")?.offsetHeight;
    if (!trHeight) return;
    const pixelToTranslate = trHeight * this.HOURS_SUBDIVISION * 24 * dayPassedPercentage;
    this.timeCursor.nativeElement.style.transform = `translateY(${pixelToTranslate}px)`;
  }

  private scrollToCurrentHour() {
    // Offset above the scroll to the current hour (in number of subdivisions)
    const OFFSET = 8;

    // Scroll to the current hour
    const currentHour = new Date().getHours();
    // Get the height of a tr
    const trHeight = this.calendarWrapper.nativeElement.querySelector("tr")?.offsetHeight;
    if (!trHeight) return;
    this.calendarWrapper.nativeElement.scrollTop = currentHour * trHeight * this.HOURS_SUBDIVISION - (OFFSET * trHeight);
  }

  private getDayName(day: number) {
    switch (day) {
      case 0:
        return "Dim.";
      case 1:
        return "Lun.";
      case 2:
        return "Mar.";
      case 3:
        return "Mer.";
      case 4:
        return "Jeu.";
      case 5:
        return "Ven.";
      case 6:
        return "Sam.";
      default:
        return "";
    }
  }

  ngAfterViewInit(): void {
    timer(0, 30_000)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(() => {
        this.placeTimeCursor();
      });
    this.scrollToCurrentHour();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
