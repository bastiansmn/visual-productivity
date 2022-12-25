import {ValidatorFn} from "@angular/forms";
import {GoalStatus} from "../model/goal.model";

export const validateStatus: ValidatorFn = (fg) => {
  const status = fg.value;

  if (Object.values(GoalStatus).indexOf(status) === -1)
    return {validateStatus: true};

  return null;
}
