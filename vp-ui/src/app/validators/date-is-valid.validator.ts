import {ValidatorFn} from "@angular/forms";

export const dateIsAfterToday: ValidatorFn = (fg) => {
  const date = new Date(fg.value);
  const today = new Date();

  return date > today ? null : {isAfter: true};
}
