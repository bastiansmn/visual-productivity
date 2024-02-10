import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {map, Subject, take, takeUntil} from "rxjs";
import {EventService} from "../../../../services/event/event.service";
import Event from "../../../../model/event.model";
import {AuthService} from "../../../../services/auth/auth.service";
import {User} from "../../../../model/user.model";
import {AlertService, AlertType} from "../../../../services/alert/alert.service";
import {MatDialog} from "@angular/material/dialog";
import {CreateEventDialogComponent} from "./create-event-dialog/create-event-dialog.component";
import {isBeforeNow, isDateBeforeNow, isPending, sameDay} from "../../../../utils/date.utils";
import {trackByEventId} from "../../../../model/time-bound-event.model";
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {DateTime} from "luxon";
import CalendarClick from "../../../common/simple-calendar/model/calendar-click.model";

@Component({
  selector: 'app-week-view',
  templateUrl: './week-view.component.html',
  styleUrls: ['./week-view.component.scss']
})
export class WeekViewComponent implements OnInit, OnDestroy {

  // Time in hours
  readonly DEFAULT_EVENT_DURATION = 2;

  @Output() weekChanged = new EventEmitter<{
    firstDate: Date,
    lastDate: Date
  }>();

  private componentDestroyed$ = new Subject<boolean>();

  days!: {date: Date, data: Event[]}[];

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
    this.fetchEvents();
  }

  nextWeek() {
    // Get the days of the next week
    this.days = this.getDaysOfWeek(new Date(this.days[6].date.getTime() + 24 * 60 * 60 * 1000));
    this.fetchEvents();
  }

  ngOnInit(): void {
    // Get the days of the current week
    this.days = this.getDaysOfWeek();
    this.fetchEvents();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  private fetchEvents() {
    this.eventService.getMyEvents(this.days[0].date, this.days[6].date)
      .pipe(take(1))
      .subscribe(this.assignEventsToDays.bind(this));
    this.weekChanged.emit({
      firstDate: this.days[0].date,
      lastDate: this.days[6].date
    });
  }

  private getDaysOfWeek(anyDayOfWeek?: Date) {
    const getMonday = (d: Date) => {
      d = new Date(d);
      const day = d.getDay(),
        diff = d.getDate() - day + (day == 0 ? -6:1); // adjust when day is sunday
      const date = new Date(d.setDate(diff));
      date.setHours(0, 0, 0, 0);
      return date;
    }

    // Initialisation
    const days: {date: Date, data: []}[] = [];
    if (!anyDayOfWeek) anyDayOfWeek = new Date();
    const mondayOfTheWeek = getMonday(anyDayOfWeek);
    for (let i = 0; i < 7; i++) {
      const day = new Date(mondayOfTheWeek.getTime() + i * 24 * 60 * 60 * 1000);
      if (i === 6) {
        day.setHours(23, 59, 59, 999);
      } else {
        day.setHours(0, 0, 0, 0);
      }
      days.push({date: day, data: []});
    }
    return days;
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
          day.data = day.data.filter(dayEvent => dayEvent.event_id !== editedEvent.event_id);
        });
        this.alertService.show(
          "Vous ne participez plus à l'événement !",
          { duration: 3000, type: AlertType.INFO }
        )
      });
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

      if (sameDay(start, end))
        day.data.push(event);
    })
  }

  createEvent($event: CalendarClick) {
    // Round the date to the nearest 15 minutes
    const dateStart = $event.date
      .minus({ minutes: $event.date.get('minute') % 15 })
      .toJSDate();
    const dateEnd = DateTime.fromJSDate(dateStart).plus({hours: this.DEFAULT_EVENT_DURATION}).toJSDate();

    const jsDayNumber = $event.date.toJSDate().getDay();
    const dayIndex = jsDayNumber > 0 ? jsDayNumber - 1 : 6;

    const day = this.days[dayIndex];

    if (isDateBeforeNow(dateStart)) return;

    const dialogRef = this.dialog.open(CreateEventDialogComponent, {
       width: '400px',
    });

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
    day.data.push(tempEvent);

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe((eventInfos: { name: string, description: string, videoCallLink: string, project: string }) => {
        day.data = day.data.filter(dayEvent => dayEvent.event_id !== tempEvent.event_id);
        if (!eventInfos) return;

        const projectId = eventInfos.project;
        tempEvent = {
          ...tempEvent,
          ...eventInfos
        };

        this.eventService.create(tempEvent, projectId)
          .pipe(take(1))
          .subscribe(event => {
            day.data.push(event);
          });
      });
  }

  deleteEvent(event: Event) {
    this.eventService.delete(event)
      .pipe(take(1))
      .subscribe(() => {
        this.days.forEach(day => {
          day.data = day.data.filter(dayEvent => dayEvent.event_id !== event.event_id);
        });
      });
  }

  protected readonly isBeforeNow = isBeforeNow;
  protected readonly trackById = trackByEventId;

  handleDrop($event: CdkDragDrop<any, any>) {
    const pixelToMinutes = $event.container.element.nativeElement.offsetHeight / (24 * 60);
    // Arrondir la valeur à 15 minutes
    const minutesShift = Math.round(($event.distance.y / pixelToMinutes) / 15) * 15;
    const dayShit = $event.container.data - $event.previousContainer.data;

    const event = $event.item.data as Event;

    let dateStart = DateTime.fromISO(new Date(($event.item.data as Event).date_start).toISOString());
    let dateEnd = DateTime.fromISO(new Date(($event.item.data as Event).date_end).toISOString());

    const eventCopy = {...event};
    eventCopy.date_start = dateStart.plus({minutes: minutesShift, days: dayShit}).toJSDate();
    eventCopy.date_end = dateEnd.plus({minutes: minutesShift, days: dayShit}).toJSDate();

    this.eventService.update(eventCopy)
      .pipe(take(1))
      .subscribe(newEvent => {
        const day = this.days[$event.container.data];
        if (!day) return;
        if ($event.container.data === $event.previousContainer.data) {
          day.data = day.data.map(e => e.event_id === newEvent.event_id ? newEvent : e);
        } else {
          day.data.push(newEvent);
          this.days[$event.previousContainer.data].data = this.days[$event.previousContainer.data].data.filter(e => e.event_id !== newEvent.event_id);
        }
      });
  }
}
