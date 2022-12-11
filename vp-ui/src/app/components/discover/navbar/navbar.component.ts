import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AuthService} from "../../../services/auth/auth.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  @Output() toggle = new EventEmitter()

  get isLoggedIn() {
    return this.authService.isLoggedIn.getValue();
  }

  constructor(private authService: AuthService) { }

  toggleNav() {
    this.toggle.emit();
  }

  ngOnInit(): void {
  }

}
