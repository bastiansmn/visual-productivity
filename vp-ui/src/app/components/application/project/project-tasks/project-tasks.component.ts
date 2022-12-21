import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProjectService} from "../../../../services/project/project.service";
import {BehaviorSubject, Subject, takeUntil} from "rxjs";
import Project from "../../../../model/project.model";

@Component({
  selector: 'app-project-tasks',
  templateUrl: './project-tasks.component.html',
  styleUrls: ['./project-tasks.component.scss']
})
export class ProjectTasksComponent implements OnInit, OnDestroy {

  private componentDestroyed$ = new Subject<boolean>();
  private project$ = new BehaviorSubject<Project | null>(null);
  get project() {
    return this.project$.getValue();
  }

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.projectService.fetchProjectById(params['id'])
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(project => {
          this.project$.next(project);
        });
    });
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
