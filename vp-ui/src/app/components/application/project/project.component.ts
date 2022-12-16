import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from "../../../services/project/project.service";
import {ActivatedRoute} from "@angular/router";
import {BehaviorSubject, Subject, takeUntil} from "rxjs";
import Project from "../../../model/project.model";

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit, OnDestroy {

  project$ = new BehaviorSubject<Project | null>(null);
  private componentDestroyed$ = new Subject<boolean>();

  constructor(
    private _projectService: ProjectService,
    private _route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this._route.params.subscribe(params => {
      if (!params.hasOwnProperty('id')) return;
      this._projectService.fetchProjectById(params['id'])
        .pipe(
          takeUntil(this.componentDestroyed$)
        )
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
