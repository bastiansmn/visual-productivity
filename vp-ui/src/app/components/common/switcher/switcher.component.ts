import {AfterViewInit, Component, ContentChild, EventEmitter, Input, Output, TemplateRef} from '@angular/core';

@Component({
  selector: 'vp-switcher',
  templateUrl: './switcher.component.html',
  styleUrls: ['./switcher.component.scss']
})
export class SwitcherComponent<T> implements AfterViewInit {

  @Input() data!: T[];
  @Input() wideButtons = false;
  @Input() defaultIndex!: number;

  @Output() switched = new EventEmitter<T>();

  @ContentChild('body',{static: false}) bodyTemplateRef!: TemplateRef<any>;

  selectElement($event: MouseEvent, item: T) {
    document.querySelectorAll(".switcher__element").forEach((element) => {
      element.classList.remove("switcher__element--selected");
    });
    ($event.target as HTMLElement).classList.add("switcher__element--selected");
    this.switched.emit(item);
  }

  ngAfterViewInit(): void {
    document.querySelectorAll(".switcher__element")[this.defaultIndex].classList.add("switcher__element--selected");
  }
}
