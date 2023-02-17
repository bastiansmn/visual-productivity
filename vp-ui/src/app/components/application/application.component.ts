import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from "../../services/project/project.service";
import {Subject, takeUntil} from "rxjs";
import {AuthService} from "../../services/auth/auth.service";

interface Link {
  url: string;
  label: string;
  icon?: string;
}

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss', '_application-theme.component.scss']
})
export class ApplicationComponent implements OnInit, OnDestroy {

  navbarToggled = false;
  componentDestroyed$ = new Subject<boolean>();

  readonly links: Link[] = [
    { url: 'dashboard', label: 'Tableau de bord', icon: 'dashboard' },
    { url: 'calendar', label: 'Calendrier', icon: 'calendar_today' },
    { url: 'todo', label: 'Todo', icon: 'assignment_turned_in' },
  ]

  get user() {
    return this.authService.loggedUser.asObservable();
  }
  get username() {
    return this.authService.loggedUser.getValue()?.name + ' ' + this.authService.loggedUser.getValue()?.lastname;
  }

  get projects() {
    return this.projectService.projects;
  }

  constructor(
    private projectService: ProjectService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.projectService.fetchProjects()
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(projects => {
        this.projectService.projects$.next(projects);
      });
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  toggleNavigation(): void {
    this.navbarToggled = !this.navbarToggled;
  }

}
