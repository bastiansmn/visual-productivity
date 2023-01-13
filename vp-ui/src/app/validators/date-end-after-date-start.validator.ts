import {ValidatorFn} from "@angular/forms";

export const isAfterDateStartDateEnd: ValidatorFn = (fg) => {
  if (fg.get('date_start')?.value === null)
    return null;

  const dateStart = new Date(fg.get('date_start')?.value);
  const dateEnd = new Date(fg.get('date_end')?.value);

  if (dateStart.getTime() > dateEnd.getTime()) {
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
