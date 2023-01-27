import {Component, EventEmitter, Input, Output} from '@angular/core';
import Label from "../../../../../model/label.model";

@Component({
  selector: 'app-label',
  templateUrl: './label.component.html',
  styleUrls: ['./label.component.scss']
})
export class LabelComponent {

  @Input() label!: Label;

  hexToRGB(hex: string, alpha?: number, darkness?: number): string {
    let r = parseInt(hex.slice(1, 3), 16),
      g = parseInt(hex.slice(3, 5), 16),
      b = parseInt(hex.slice(5, 7), 16);

    if (alpha)
      return "rgba(" + r*(darkness??1) + ", " + g*(darkness??1) + ", " + b*(darkness??1) + ", " + alpha + ")";
    return "rgb(" + r*(darkness??1) + ", " + g*(darkness??1) + ", " + b*(darkness??1) + ")";
  }

}
