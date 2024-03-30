import Event from "./event.model";

export default interface TimeBoundedEvent {
  date_start: Date;
  date_end: Date;
}

export const trackByEventId = (data1: Event, data2: Event) => data1.event_id === data2.event_id;
export const trackById = (data1: any, data2: any) => data1.id === data2.id;
