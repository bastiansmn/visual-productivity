import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from "../../../../services/project/project.service";
import {BehaviorSubject, catchError, EMPTY, Subject, takeUntil} from "rxjs";
import Project from "../../../../model/project.model";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {AddUserDialogComponent} from "./add-user-dialog/add-user-dialog.component";
import {AlertService, AlertType} from "../../../../services/alert/alert.service";
import {LoaderService} from "../../../../services/loader/loader.service";
import Goal, {GoalStatus} from "../../../../model/goal.model";

@Component({
  selector: 'app-project-dashboard',
  templateUrl: './project-dashboard.component.html',
  styleUrls: ['./project-dashboard.component.scss']
})
export class ProjectDashboardComponent implements OnInit, OnDestroy {

  private componentDestroyed$ = new Subject<boolean>();
  private project$ = new BehaviorSubject<Project | null>(null);
  get project() {
    return this.project$.getValue();
  }
  private goalsGrouped$ = new BehaviorSubject({
    [GoalStatus.TODO]: [] as Goal[],
    [GoalStatus.IN_PROGRESS.toString()]: [] as Goal[],
    [GoalStatus.DONE.toString()]: [] as Goal[]
  })
  get goalsGrouped() {
    return this.goalsGrouped$.getValue();
  }
  goalsByStatus(status: string) {
    return this.goalsGrouped[status];
  }
  GoalStatus = GoalStatus;
  enumIterator = Object.keys(GoalStatus);

  constructor(
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  toggleDialog() {
    const dialogRef = this.dialog.open(AddUserDialogComponent, {
      data: this.project
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!this.project?.projectId) return;
      if (!result) return;
      if (!result.match('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')) return;

      this.projectService.addUserInProject(result, this.project.projectId)
        .pipe(
          takeUntil(this.componentDestroyed$),
          catchError(err => {
            this.loaderService.hide();
            this.alertService.show(
              err,
              { type: AlertType.ERROR, duration: 5000 }
            )
            return EMPTY;
          })
        )
        .subscribe(user => {
          this.loaderService.hide();
          this.alertService.show(
            "L'invitation a été envoyé à l'utilisateur",
            { type: AlertType.SUCCESS, duration: 5000 }
          )
          this.projectService.projects.find(p => p.projectId === this.project?.projectId)?.users.push(user);
        })
    });
  }

  ngOnInit(): void {
    this.loaderService.show();
    this.route.params
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(params => {
        this.loaderService.hide();
        this.projectService.projects$.pipe(takeUntil(this.componentDestroyed$))
          .subscribe(projects => {
            this.loaderService.hide();
            this.project$.next(projects.find(p => p.projectId === params['id']) ?? null);
            this.goalsGrouped$.next({
              [GoalStatus.TODO]: this.project?.allGoals.filter(g => g.status === GoalStatus.TODO) ?? [],
              [GoalStatus.IN_PROGRESS]: this.project?.allGoals?.filter(g => g.status === GoalStatus.IN_PROGRESS) ?? [],
              [GoalStatus.DONE]: this.project?.allGoals?.filter(g => g.status === GoalStatus.DONE) ?? []
            });
          })
      });
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
