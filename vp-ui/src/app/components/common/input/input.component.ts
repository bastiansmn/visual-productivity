import {Component, Input} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'vp-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss', '_input-theme.component.scss']
})
export class InputComponent {

  @Input() type: string = "text";
  @Input() label!: string;
  @Input() placeholder!: string;
  @Input() name!: string;
  @Input() spellcheck: boolean = false;
  @Input() disabled: boolean = false;
  @Input() required: boolean = false;
  @Input() autocomplete: boolean = false;
  @Input() datalist: Array<string> = [];
  @Input() pattern: string = ".*";
  @Input() tabulationIndex: string = "-1";

  // Form relative Input()
  @Input() parentForm!: FormGroup;
  @Input() parentFormControlName!: string;

}
