import { Pipe, PipeTransform } from '@angular/core';
import {User} from "../model/user.model";

@Pipe({
  name: 'firstLetters'
})
export class FirstLettersPipe implements PipeTransform {

  transform(value: User | null, ...args: unknown[]): string {
    if (!value) return '';
    return value.name[0] + value.lastname[0];
  }

}
