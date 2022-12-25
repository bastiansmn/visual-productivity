import {ValidatorFn} from "@angular/forms";

export const isBeforeDeadline: ValidatorFn = (fg) => {
  if (!fg.get('deadline')?.value)
    return null;

  const dateStart = new Date(fg.get('date_start')?.value);
  const deadline = new Date(fg.get('deadline')?.value);

  if (dateStart.getTime() >= deadline.getTime()) {
    fg.get('deadline')?.setErrors({
      dateValidation: true,
      ...fg.get('deadline')?.errors
    });
    return {dateValidation: true};
  }

  const {dateValidation, ...errors} = fg.get('deadline')?.errors || {};
  fg.get('deadline')?.setErrors(
    Object.keys(errors).length > 0 ? errors : null
  );
  return null;
}
