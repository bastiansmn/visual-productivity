import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'emailPrivacy'
})
export class EmailPrivacyPipe implements PipeTransform {

  transform(value: string | undefined, ...args: unknown[]): string {
    if (!value) return '';
    return value
      .replace(/(?<=.{5}).*(?=@)/g, '*'.repeat(5));
  }

}
