import {ValidatorFn} from "@angular/forms";

export const isAfterDateStart: ValidatorFn = (fg) => {
  if (fg.get('date_start')?.value === null)
    return null;

  const dateStart = new Date(fg.get('date_start')?.value);
  const deadline = new Date(fg.get('deadline')?.value);

  if (dateStart.getTime() > deadline.getTime()) {
    fg.get('date_start')?.setErrors({
      dateValidation: true,
      ...fg.get('date_start')?.errors
    });
    return {dateValidation: true};
  }

  const {dateValidation, ...errors} = fg.get('date_start')?.errors || {};
  fg.get('date_start')?.setErrors(
    Object.keys(errors).length > 0 ? errors : null
  );
  return null;
}
