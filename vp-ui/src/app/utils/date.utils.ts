import TimeBoundedEvent from "../model/time-bound-event.model";

export function sameDay(d1: Date, d2: Date) {
  return d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth() && d1.getDate() === d2.getDate();
}

export function isBeforeNow(event: TimeBoundedEvent) {
  return new Date(event.date_end).getTime() < new Date().getTime();
}

export function isDateBeforeNow(date: Date) {
  return date.getTime() < new Date().getTime();
}

export function isPending(event: TimeBoundedEvent) {
  return new Date(event.date_end).getTime() > new Date().getTime() && new Date(event.date_start).getTime() < new Date().getTime();
}
