import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from "../../services/project/project.service";
import Project from "../../model/project.model";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss', '_application-theme.component.scss']
})
export class ApplicationComponent implements OnInit, OnDestroy {

  navbarToggled = false;
  componentDestroyed$ = new Subject<boolean>();

  get projects() {
    return this.projectService.projects;
  }

  constructor(
    private projectService: ProjectService
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
