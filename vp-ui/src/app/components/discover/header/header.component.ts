import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Output() toggleNavigation = new EventEmitter();

  constructor() { }

  emitToggleNavigation() {
    this.toggleNavigation.emit(null);
  }

  ngOnInit(): void {
  }

}
