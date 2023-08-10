import {AfterViewInit, Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, ViewChild} from '@angular/core';
import {BehaviorSubject, map, Subject, take, takeUntil, timer} from "rxjs";
import {EventService} from "../../../../services/event/event.service";
import Event from "../../../../model/event.model";
import {AuthService} from "../../../../services/auth/auth.service";
import {User} from "../../../../model/user.model";
import {AlertService, AlertType} from "../../../../services/alert/alert.service";
import {MatDialog} from "@angular/material/dialog";
import {CreateEventDialogComponent} from "./create-event-dialog/create-event-dialog.component";
import {CreateEventData} from "./create-event-dialog/create-event.data";

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

  days!: {name: string, date: Date, current: boolean, events: Event[]}[];

  private tempCreatingEvent$ = new BehaviorSubject<Event | null>(null);
  get tempCreatingEvent() {
    return this.tempCreatingEvent$.asObservable();
  }

  hours = [ "00:00", "00:15", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00", "02:15", "02:30", "02:45", "03:00", "03:15", "03:30", "03:45", "04:00", "04:15", "04:30", "04:45", "05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45", "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45", "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45", "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45", "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45", "23:00", "23:15", "23:30", "23:45" ];
  // Number of subdivisions per hour
  readonly HOURS_SUBDIVISION = 4;

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private alertService: AlertService,
    private dialog: MatDialog
  ) { }

  prevWeek() {
    // Get the days of the previous week
    this.days = this.getDaysOfWeek(new Date(this.days[0].date.getTime() - 24 * 60 * 60 * 1000));
    this.fetchMyEvents(this.days[0].date, this.days[6].date)
      .subscribe(this.assignEventsToDays.bind(this));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  nextWeek() {
    // Get the days of the next week
    this.days = this.getDaysOfWeek(new Date(this.days[6].date.getTime() + 24 * 60 * 60 * 1000));
    this.fetchMyEvents(this.days[0].date, this.days[6].date)
      .subscribe(this.assignEventsToDays.bind(this));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  private sameDay(d1: Date, d2: Date) {
    return d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth() && d1.getDate() === d2.getDate();
  }

  ngOnInit(): void {
    // Get the days of the current week
    this.days = this.getDaysOfWeek();
    this.fetchMyEvents(this.days[0].date, this.days[6].date)
      .subscribe(this.assignEventsToDays.bind(this));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  private fetchMyEvents(from: Date, to: Date) {
    return this.eventService.getMyEvents(from, to)
      .pipe(take(1))
  }

  private getDaysOfWeek(anyDayOfWeek?: Date) {
    const getMonday = (d: Date) => {
      d = new Date(d);
      var day = d.getDay(),
        diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
      return new Date(d.setDate(diff));
    }

    // Initialisation
    const days: {name: string, date: Date, current: boolean, events: []}[] = [];
    if (!anyDayOfWeek) anyDayOfWeek = new Date();
    const mondayOfTheWeek = getMonday(anyDayOfWeek);
    for (let i = 0; i < 7; i++) {
      const day = new Date(mondayOfTheWeek.getTime() + i * 24 * 60 * 60 * 1000);
      // Check if day and new Date() are same day
      const current = day.getDate() === new Date().getDate() && day.getMonth() === new Date().getMonth() && day.getFullYear() === new Date().getFullYear();
      days.push({name: this.getDayName(day.getDay()), date: day, current, events: []});
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

  computeEventInset(event: Event) {
    const trHeight = this.calendarWrapper.nativeElement.querySelector("tr")?.offsetHeight;
    if (!trHeight) return { inset: '' };

    const dateStart = new Date(event.date_start);
    const dateEnd = new Date(event.date_end);

    const minutesPixelSize = trHeight * this.HOURS_SUBDIVISION / 60;
    const minutesStart = dateStart.getMinutes() + dateStart.getHours() * 60;
    const minutesEnd = dateEnd.getMinutes() + dateEnd.getHours() * 60;
    const wrapperTotalHeight = this.calendarWrapper.nativeElement.querySelector(".calendar__content")?.clientHeight;
    if (!wrapperTotalHeight) return { inset: '' };

    return { inset: `${minutesPixelSize * minutesStart}px 0% ${wrapperTotalHeight - (minutesPixelSize*minutesEnd)}px` };
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

  getOtherUsers(participants: User[]) {
    return this.authService.loggedUser
      .pipe(
        takeUntil(this.componentDestroyed$),
        map(user =>
          participants.filter(participant => participant.user_id !== user?.user_id)
        )
      );
  }

  handleClick(videoCallLink: string) {
    window.open(videoCallLink, '_blank');
  }

  unparticipate(event: Event) {
    this.eventService.unparticipate(event)
      .pipe(take(1))
      .subscribe(editedEvent => {
        // Remove event from days.events
        this.days.forEach(day => {
          day.events = day.events.filter(dayEvent => dayEvent.event_id !== editedEvent.event_id);
        });
        this.alertService.show(
          "Vous ne participez plus à l'événement !",
          { duration: 3000, type: AlertType.INFO }
        )
      });
  }

  computeEventPosition(index: number) {
    if (index >= 4)
      return "right: 105%"
    return "left: 105%"
  }

  private assignEventsToDays(events: Event[]) {
    events.forEach(event => {
      if (event.whole_day) {
        // TODO
        return;
      }

      const start = new Date(event.date_start);
      const end = new Date(event.date_end);
      const day = this.days.find(day => day.date.getDate() === start.getDate() && day.date.getMonth() === start.getMonth() && day.date.getFullYear() === start.getFullYear());
      if (!day) return;

      if (this.sameDay(start, end))
        day.events.push(event);
    })
  }

  createEvent($event: MouseEvent) {
    const target = $event.target as HTMLElement;
    if (!target) return;
    // Get the bounding rectangle of target
    const rect = target.getBoundingClientRect();
    // Mouse position
    const x = $event.clientX - rect.left;
    const dayIndex = Math.floor((x / rect.width) * 7);
    const timeStart = target.getAttribute("data-time");
    if (!timeStart) return;

    // Create a new Date with day.date (the day) and timeStart (the time)
    const day = this.days[dayIndex];

    const dialogRef = this.dialog.open(CreateEventDialogComponent, {
      data: {
        date: day.date,
        timeStart: timeStart,
      } as CreateEventData
    })

    const dateStart = new Date(day.date);
    const timeStartSplit = timeStart.split(":");
    dateStart.setHours(+timeStartSplit[0]);
    dateStart.setMinutes(+timeStartSplit[1]);
    dateStart.setSeconds(0);
    const dateEnd = new Date(dateStart);
    dateEnd.setHours(dateEnd.getHours() + 2);

    let tempEvent: Event = {
      event_id: -1,
      description: "",
      name: "",
      participating: true,
      videoCallLink: "",
      whole_day: false,
      participants: [],
      date_start: dateStart,
      date_end: dateEnd,
      createdByMe: true,
    }
    day.events.push(tempEvent);

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe((eventInfos: { name: string, description: string, videoCallLink: string, project: string }) => {
        day.events = day.events.filter(dayEvent => dayEvent.event_id !== tempEvent.event_id);
        if (!eventInfos) return;

        const projectId = eventInfos.project;
        tempEvent = {
          ...tempEvent,
          ...eventInfos
        };

        this.eventService.create(tempEvent, projectId)
          .pipe(take(1))
          .subscribe(event => {
            day.events.push(event);
          });
      });
  }

  deleteEvent(event: Event) {
    this.eventService.delete(event)
      .pipe(take(1))
      .subscribe(() => {
        this.days.forEach(day => {
          day.events = day.events.filter(dayEvent => dayEvent.event_id !== event.event_id);
        });
      });
  }

  isBeforeNow(event: Event) {
    return new Date(event.date_end).getTime() < new Date().getTime();
  }
}
