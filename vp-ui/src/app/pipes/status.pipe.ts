import { Pipe, PipeTransform } from '@angular/core';
import {GoalStatus} from "../model/goal.model";

@Pipe({
  name: 'statusDisplay'
})
export class StatusPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch (value) {
      case GoalStatus.TODO:
        return "A faire";
      case GoalStatus.IN_PROGRESS:
        return 'En cours';
      case GoalStatus.DONE:
        return 'Terminé';
      default:
        return 'Inconnu';
    }
  }

}
