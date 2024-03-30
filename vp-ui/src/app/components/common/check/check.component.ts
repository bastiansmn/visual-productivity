import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'vp-check',
  templateUrl: './check.component.html',
  styleUrls: ['./check.component.scss']
})
export class CheckComponent implements OnInit {

  @Input() name!: string;
  @Input() disabled: boolean = false;
  @Input() required: boolean = false;
  @Input() tabulationIndex: string =" -1";
  @Input() checked: boolean = false;

  // Form relative Input()
  @Input() parentForm!: FormGroup;
  @Input() parentFormControlName!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
