import {Component, Input} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'vp-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss', '_select-theme.component.scss']
})
export class SelectComponent {

  @Input() name!: string;
  @Input() label!: string;
  @Input() placeholder!: string;
  @Input() options!: { value: string, label: string }[];
  @Input() parentForm!: FormGroup;
  @Input() parentFormControlName!: string;

  @Input() tabulationIndex = '-1';
  @Input() required = false;

}
