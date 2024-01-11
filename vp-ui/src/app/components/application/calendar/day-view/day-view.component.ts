import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {EventService} from "../../../../services/event/event.service";
import {AuthService} from "../../../../services/auth/auth.service";
import {MatDialog} from "@angular/material/dialog";
import {map, Subject, take, takeUntil} from "rxjs";
import Event from "../../../../model/event.model";
import {User} from "../../../../model/user.model";
import {EventInformationsComponent} from "../event-informations/event-informations.component";
import {isBeforeNow, isDateBeforeNow, isPending, sameDay} from "../../../../utils/date.utils";
import {trackByEventId} from "../../../../model/time-bound-event.model";
import {CdkDragDrop} from "@angular/cdk/drag-drop";
import {CreateEventDialogComponent} from "../week-view/create-event-dialog/create-event-dialog.component";
import {CreateEventData} from "../week-view/create-event-dialog/create-event.data";

@Component({
  selector: 'app-day-view',
  templateUrl: './day-view.component.html',
  styleUrls: ['./day-view.component.scss']
})
export class DayViewComponent implements OnInit {

  private componentDestroyed$ = new Subject<boolean>();

  days!: {date: Date, data: Event[]}[];

  // Number of subdivisions per hour
  readonly HOURS_SUBDIVISION = 4;

  @Output() dayLoaded = new EventEmitter<Date>();

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    const from = new Date();
    from.setHours(0, 0, 0, 0);
    const to = new Date();
    to.setHours(23, 59, 59, 999);

    this.days = [ { date: from, data: [] } ];

    this.fetchEvents(from, to);
  }

  private fetchEvents(from: Date, to: Date) {
    this.eventService.getMyEvents(from, to)
      .pipe(take(1))
      .subscribe(this.assignEventsToDays.bind(this));
    this.dayLoaded.emit(from);
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

  getOtherUsers(participants: User[]) {
    return this.authService.loggedUser
      .pipe(
        takeUntil(this.componentDestroyed$),
        map(user =>
          participants.filter(participant => participant.user_id !== user?.user_id)
        )
      );
  }

  previousDay() {
    const from = this.days[0].date;
    from.setDate(from.getDate() - 1);
    from.setHours(0, 0, 0, 0);
    const to = new Date(from);
    to.setHours(23, 59, 59, 999);

    this.days = [ { date: from, data: [] } ];

    this.fetchEvents(from, to);
  }

  nextDay() {
    const from = this.days[0].date;
    from.setDate(from.getDate() + 1);
    from.setHours(0, 0, 0, 0);
    const to = new Date(from);
    to.setHours(23, 59, 59, 999);

    this.days = [ { date: from, data: [] } ];

    this.fetchEvents(from, to);
  }

  openModal(event: Event) {
    console.log(event);
    const dialogRef = this.dialog.open(EventInformationsComponent, {
      data: event,
    })

    dialogRef.afterClosed()
      .pipe(take(1))
      .subscribe()
  }

  protected readonly isBeforeNow = isBeforeNow;
  protected readonly isPending = isPending;
  protected readonly trackById = trackByEventId;

  createEvent($event: MouseEvent) {
    const target = $event.target as HTMLElement;
    if (!target) return;
    // Get the bounding rectangle of target
    const rect = target.getBoundingClientRect();
    // Mouse position
    const x = $event.clientX - rect.left;
    const timeStart = target.getAttribute("data-time");
    if (!timeStart) return;

    // Create a new Date with day.date (the day) and timeStart (the time)
    const day = this.days[0];

    const dateStart = new Date(day.date);
    const timeStartSplit = timeStart.split(":");
    dateStart.setHours(+timeStartSplit[0]);
    dateStart.setMinutes(+timeStartSplit[1]);
    dateStart.setSeconds(0);
    const dateEnd = new Date(dateStart);
    dateEnd.setHours(dateEnd.getHours() + 2);

    if (isDateBeforeNow(dateStart)) return;

    const dialogRef = this.dialog.open(CreateEventDialogComponent, {
      data: {
        date: day.date,
        timeStart: timeStart,
      } as CreateEventData
    })


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

  handleDrop($event: CdkDragDrop<any, any>) {
    const pixelToMinutes = $event.container.element.nativeElement.offsetHeight / (24 * 60);
    // Arrondir la valeur à 15 minutes
    const minutes = Math.round(($event.distance.y / pixelToMinutes) / 15) * 15;

    const event = $event.item.data as Event;
    const dateStart = new Date(($event.item.data as Event).date_start);
    const dateEnd = new Date(($event.item.data as Event).date_end);
    dateStart.setMinutes(dateStart.getMinutes() + minutes);
    dateEnd.setMinutes(dateEnd.getMinutes() + minutes);

    const eventCopy = {...event};
    eventCopy.date_start = dateStart;
    eventCopy.date_end = dateEnd;

    this.eventService.update(eventCopy)
      .pipe(take(1))
      .subscribe(newEvent => {
        const day = this.days.find(day => day.date.getDate() === new Date(newEvent.date_start).getDate() && day.date.getMonth() === new Date(newEvent.date_start).getMonth() && day.date.getFullYear() === new Date(newEvent.date_start).getFullYear());
        if (!day) return;
        day.data = day.data.map(e => e.event_id === newEvent.event_id ? newEvent : e);
      });
  }
}
