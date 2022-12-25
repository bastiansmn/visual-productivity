import {ValidatorFn} from "@angular/forms";

export const dateIsAfterToday: ValidatorFn = (fg) => {
  const date = new Date(fg.value);
  const today = new Date();
  today.setUTCHours(0, 0, 0, 0);

  return date.getTime() >= today.getTime() ? null : {isAfter: true};
}
