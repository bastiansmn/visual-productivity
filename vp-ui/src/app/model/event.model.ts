import {User} from "./user.model";

export default interface Event {
  event_id: number;
  name: string;
  description: string;
  videoCallLink: string;
  date_start: Date;
  date_end: Date;
  whole_day: boolean;
  participants: User[];
  participating: boolean;
}
