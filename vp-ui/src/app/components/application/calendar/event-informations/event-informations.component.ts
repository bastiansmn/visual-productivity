import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Event from "../../../../model/event.model";
import {take} from "rxjs";
import {AlertService, AlertType} from "../../../../services/alert/alert.service";
import {EventService} from "../../../../services/event/event.service";
import {isBeforeNow} from "../../../../utils/date.utils";

@Component({
  selector: 'app-event-informations',
  templateUrl: './event-informations.component.html',
  styleUrls: ['./event-informations.component.scss']
})
export class EventInformationsComponent {

  constructor(
    private eventService: EventService,
    private alertService: AlertService,
    public dialogRef: MatDialogRef<EventInformationsComponent>,
    @Inject(MAT_DIALOG_DATA) public event: Event,
  ) {}

  unparticipate(event: Event) {
    this.eventService.unparticipate(event)
      .pipe(take(1))
      .subscribe(editedEvent => {
        // Remove event from days.events
        this.alertService.show(
          "Vous ne participez plus à l'événement !",
          { duration: 3000, type: AlertType.INFO }
        )
        this.dialogRef.close(event);
      });
  }

  deleteEvent(event: Event) {
    this.eventService.delete(event)
      .pipe(take(1))
      .subscribe(() => {
        this.dialogRef.close(event);
      });
  }

  protected readonly isBeforeNow = isBeforeNow;
}
