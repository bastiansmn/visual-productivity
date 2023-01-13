import {ValidatorFn} from "@angular/forms";

export const isBeforeDateEnd: ValidatorFn = (fg) => {
  if (!fg.get('date_end')?.value)
    return null;

  const dateStart = new Date(fg.get('date_start')?.value);
  const dateEnd = new Date(fg.get('date_end')?.value);

  if (dateStart.getTime() >= dateEnd.getTime()) {
    fg.get('date_end')?.setErrors({
      dateValidation: true,
      ...fg.get('date_end')?.errors
    });
    return {dateValidation: true};
  }

  const {dateValidation, ...errors} = fg.get('date_end')?.errors || {};
  fg.get('date_end')?.setErrors(
    Object.keys(errors).length > 0 ? errors : null
  );
  return null;
}
