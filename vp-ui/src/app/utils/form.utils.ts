import {AbstractControl, FormArray, FormGroup} from "@angular/forms";

export const markAllAsDirty = (control: AbstractControl) => {
  if (control instanceof FormGroup) {
    Object.values(control.controls).forEach(control => {
      markAllAsDirty(control);
    });
    return;
  }

  if (control instanceof FormArray) {
    control.controls.forEach((control) => markAllAsDirty(control));
    return;
  }

  control.markAsDirty();
}
