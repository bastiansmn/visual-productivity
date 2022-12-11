import Goal from "./goal.model";

export default interface Event {
  event_id: number;
  name: string;
  description: string;
  date_start: Date;
  date_end: Date;
  whole_day: boolean;
  goals: Goal[];
}
