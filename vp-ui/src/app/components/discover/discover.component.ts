import {Component, ViewChild} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";

@Component({
  selector: 'app-discover',
  templateUrl: './discover.component.html',
  styleUrls: ['./discover.component.scss', './_discover-theme.component.scss']
})
export class DiscoverComponent {

  @ViewChild('sidenav') public sidenav!: MatSidenav;
  opened: boolean = false;

  toggleNavigation(): void {
    this.sidenav.toggle();
    this.opened = !this.opened;
  }

}
