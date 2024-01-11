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

    const switcherElement = this.findSwitcherElement($event.target as HTMLElement);
    // Recursively search for the switcher element
    switcherElement?.classList.add("switcher__element--selected");
    this.switched.emit(item);
  }

  private findSwitcherElement(element: HTMLElement): HTMLElement | null {
    if (element.classList.contains("switcher__element")) {
      return element;
    } else {
      return this.findSwitcherElement(element.parentElement!);
    }
  }

  ngAfterViewInit(): void {
    document.querySelectorAll(".switcher__element")[this.defaultIndex].classList.add("switcher__element--selected");
  }
}
