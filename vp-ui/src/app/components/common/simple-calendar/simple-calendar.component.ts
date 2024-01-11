import {
  AfterViewInit,
  Component,
  ContentChild,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  Output,
  TemplateRef,
  ViewChild
} from '@angular/core';
import {Subject, takeUntil, timer} from "rxjs";
import TimeBoundedEvent from "../../../model/time-bound-event.model";
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {isBeforeNow, isPending} from "../../../utils/date.utils";

@Component({
  selector: 'app-simple-calendar',
  templateUrl: './simple-calendar.component.html',
  styleUrls: ['./simple-calendar.component.scss']
})
export class SimpleCalendarComponent implements AfterViewInit, OnDestroy {

  private componentDestroyed$ = new Subject<boolean>();

  @ViewChild("calendarWrapper") calendarWrapper!: ElementRef<HTMLDivElement>
  @ViewChild("timeCursor") timeCursor!: ElementRef<HTMLDivElement>

  @Input() values!: {date: Date, data: TimeBoundedEvent[]}[];
  // Number of subdivisions per hour
  @Input() hoursSubdivision = 4;
  @Input() hours = [ "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45" ];

  @Output() previous = new EventEmitter<void>();
  _previous() {
    this.previous.emit();
  }

  @Output() next = new EventEmitter<void>();
  _next() {
    this.next.emit();
  }

  @Output() click = new EventEmitter<MouseEvent>();
  _click($event: MouseEvent) {
    $event.stopPropagation();
    this.click.emit($event);
  }

  @Output() drop = new EventEmitter<CdkDragDrop<any, any>>();

  @ContentChild(TemplateRef) contentRef!: TemplateRef<any>;
  @ContentChild("details") detailsRef!: TemplateRef<any>;
  @ContentChild("placeholder") placeholderRef!: TemplateRef<any>;
  @ContentChild("preview") previewRef!: TemplateRef<any>;

  @Input() cdkDragDisabled: (...args: any[]) => boolean = () => false;

  @Input() trackByFn: (data1: any, data2: any) => boolean = (data1, data2) => data1 === data2;
  _trackByFn = this.trackByFn.bind(this);

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

  getDayName(day: number) {
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

  isCurrentDay(day: Date) {
    return day.getDate() === new Date().getDate() && day.getMonth() === new Date().getMonth() && day.getFullYear() === new Date().getFullYear()
  }

  private placeTimeCursor() {
    const currentHour = new Date().getHours();
    const currentMinute = new Date().getMinutes();
    const dayPassedPercentage = (currentHour * 60 + currentMinute) / (24*60);

    // Get the height of a tr
    const trHeight = this.calendarWrapper.nativeElement.querySelector("tr")?.offsetHeight;
    if (!trHeight) return;
    const pixelToTranslate = trHeight * this.hoursSubdivision * 24 * dayPassedPercentage;
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
    this.calendarWrapper.nativeElement.scrollTop = currentHour * trHeight * this.hoursSubdivision - (OFFSET * trHeight);
  }

  computeStyle(data: TimeBoundedEvent, i: number) {
    const trHeight = this.calendarWrapper.nativeElement.querySelector("tr")?.offsetHeight;
    if (!trHeight) return { inset: '' };

    const dateStart = new Date(data.date_start);
    const dateEnd = new Date(data.date_end);
    if (dateStart.getDate() !== dateEnd.getDate()) {
      dateEnd.setHours(23, 59, 59, 999);
    }

    const minutesPixelSize = trHeight * this.hoursSubdivision / 60;
    const minutesStart = dateStart.getMinutes() + dateStart.getHours() * 60;
    const minutesEnd = dateEnd.getMinutes() + dateEnd.getHours() * 60;
    const wrapperTotalHeight = this.calendarWrapper.nativeElement.querySelector(".calendar__content")?.clientHeight;
    if (!wrapperTotalHeight) return { inset: '' };

    const eventsOfDay = this.values
      .find(e => e.date.getDate() === dateStart.getDate() && e.date.getMonth() === dateStart.getMonth() && e.date.getFullYear() === dateStart.getFullYear())!.data;

    const eventsWithSameStart = eventsOfDay
      .filter(e =>
        (new Date(data.date_start).getTime() < new Date(e.date_start).getTime() && new Date(data.date_end).getTime() > new Date(e.date_start).getTime())
        || new Date(e.date_start).getTime() < new Date(data.date_start).getTime() && new Date(e.date_end).getTime() > new Date(data.date_start).getTime()
      );

    const conflictingEvents = [...eventsWithSameStart, data]
      .sort((a, b) => new Date(a.date_start).getTime() - new Date(b.date_start).getTime());

    const index = conflictingEvents.findIndex(e => e.date_start === data.date_start && e.date_end === data.date_end);

    return {
      'position': 'absolute',
      'width.%': 100 / (eventsWithSameStart.length + 1),
      // 'top.%': 0,
      // 'right': `${Math.round((100 / (eventsWithSameStart.length + 1) * Math.max(index, 0)) * 100) / 100}%`,
      'transform': `translateX(${index * 100}%)`,
      'height.px': minutesPixelSize * (minutesEnd - minutesStart),
      'inset': `${minutesPixelSize * minutesStart}px 0% ${wrapperTotalHeight - (minutesPixelSize*minutesEnd)}px`,
      'z-index': 99+i
    };
  }

  getDropListId(index: number) {
    return `drop-list-${index}`;
  }

  getConnectedDropLists(index: number) {
    return [...Array(this.values.length).keys()]
      .filter(i => i !== index)
      .map(i => this.getDropListId(i))
  }

  handleDrop($event: CdkDragDrop<any, any>) {
    this.drop.emit($event);
  }

  toggleDetails(detailsRef: HTMLElement, $event: MouseEvent) {
    $event.stopPropagation();
    detailsRef.classList.toggle("visible");
  }

  protected readonly isBeforeNow = isBeforeNow;
  protected readonly isPending = isPending;
}
