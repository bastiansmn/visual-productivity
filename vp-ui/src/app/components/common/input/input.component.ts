import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'vp-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss', '_input-theme.component.scss']
})
export class InputComponent {

  @Input() type: String = "text";
  @Input() label!: String;
  @Input() placeholder!: String;
  @Input() name!: String;
  @Input() spellcheck: boolean = false;
  @Input() disabled: boolean = false;
  @Input() required: boolean = false;
  @Input() autocomplete: boolean = false;
  @Input() datalist: Array<String> = [];
  @Input() pattern: string = ".*";
  @Input() tabulationIndex: string = "-1";

  // Form relative Input()
  @Input() parentForm!: FormGroup;
  @Input() parentFormControlName!: string;

}
