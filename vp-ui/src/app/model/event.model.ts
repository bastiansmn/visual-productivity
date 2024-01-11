import {User} from "./user.model";
import TimeBoundedEvent from "./time-bound-event.model";

export default interface Event extends TimeBoundedEvent {
  event_id: number;
  name: string;
  description: string;
  videoCallLink: string;

  // Inherited from TimeBoundedEvent
  // date_start: Date;
  // date_end: Date;

  whole_day: boolean;
  participants: User[];
  participating: boolean;
  createdByMe: boolean;
}
