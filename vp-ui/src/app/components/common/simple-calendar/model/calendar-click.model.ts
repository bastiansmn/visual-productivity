import {DateTime} from "luxon";

export default interface CalendarClick {
  mouseevent: MouseEvent;
  date: DateTime;
}
