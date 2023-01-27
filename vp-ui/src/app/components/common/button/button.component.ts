import {AfterViewInit, Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';

@Component({
  selector: 'vp-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})
export class ButtonComponent implements AfterViewInit {

  @Input() color: string = "light"
  @Input() background: string = "purple"
  @Input() hoverBgColor: string = "light"
  @Input() hoverColor: string = "purple"
  @Input() border: boolean = false;
  @Input() hoverBorder: boolean = true;
  @Input() disabled: boolean = false;
  @Input() scaleOnHover: boolean = false;
  @Input() tabulationIndex: string = "-1";
  @Input() padding: boolean = true;
  @Input() rounded: boolean = false;

  @ViewChild('button') button!: ElementRef

  @Output() clicked = new EventEmitter()

  constructor() { }

  handleClick() {
    this.clicked.emit();
  }

  ngAfterViewInit(): void {
    this.button.nativeElement.classList.add(
      `text-${this.color}`,
      `bg-${this.background}`,
      `hovBG-${this.hoverBgColor}`,
      `hov-${this.hoverColor}`
    );
    if (this.border)
      this.button.nativeElement.classList.add('border')
    if (this.hoverBorder)
      this.button.nativeElement.classList.add('hov-border')
    if (this.scaleOnHover)
      this.button.nativeElement.classList.add('hov-scale')
    if (this.padding)
      this.button.nativeElement.classList.add('padding-inline')
    if (this.rounded)
      this.button.nativeElement.classList.add('rounded')
  }

}
