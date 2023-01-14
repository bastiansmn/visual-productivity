import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectService} from "../../../../services/project/project.service";
import {BehaviorSubject, catchError, EMPTY, Subject, take, takeUntil} from "rxjs";
import Project from "../../../../model/project.model";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {AddUserDialogComponent} from "./add-user-dialog/add-user-dialog.component";
import {AlertService, AlertType} from "../../../../services/alert/alert.service";
import {LoaderService} from "../../../../services/loader/loader.service";
import Goal, {GoalStatus} from "../../../../model/goal.model";
import {AddGoalDialogComponent} from "./add-goal-dialog/add-goal-dialog.component";
import {GoalService} from "../../../../services/goal/goal.service";
import {CdkDragDrop, CdkDragEnd, CdkDragStart, transferArrayItem} from "@angular/cdk/drag-drop";

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
    return this.goalsGrouped[status].sort((g1, g2) => new Date(g1.deadline).getTime() - new Date(g2.deadline).getTime());
  }
  GoalStatus = GoalStatus;
  enumIterator = Object.keys(GoalStatus);
  goal$ = new BehaviorSubject<Goal | null>(null);
  get goal() {
    return this.goal$.getValue();
  }
  get goalShown() {
    return !!this.goal;
  }

  constructor(
    private projectService: ProjectService,
    private goalService: GoalService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  toggleAddProjectDialog() {
    const dialogRef = this.dialog.open(AddUserDialogComponent, {
      data: this.project,
      disableClose: true
    });

    dialogRef.afterClosed().pipe(take(1)).subscribe(result => {
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
          if (!user) {
            this.alertService.show(
              "L'utilisateur n'a pas de compte, un mail lui a quand même été envoyé",
              { type: AlertType.WARNING, duration: 5000 }
            );
            return;
          }
          this.alertService.show(
            "L'invitation a été envoyé à l'utilisateur",
            { type: AlertType.SUCCESS, duration: 5000 }
          )
          this.projectService.projects.find(p => p.projectId === this.project?.projectId)?.users.push(user);
        })
    });
  }

  toggleAddGoalDialog(status: string) {
    if (this.enumIterator.indexOf(status) === -1) return;

    const dialogRef = this.dialog.open(AddGoalDialogComponent, {
      data: { project: this.project, type: status},
      disableClose: true
    })

    dialogRef.afterClosed().pipe(take(1)).subscribe(result => {
      if (!result) return;

      this.goalService.addGoalInProject(result)
        .pipe(takeUntil(this.componentDestroyed$))
        .subscribe(goal => {
          this.projectService.projects.find(p => p.projectId === this.project?.projectId)?.allGoals.push(goal);
          this.goalsGrouped$.next({
            ...this.goalsGrouped,
            [goal.status]: [...this.goalsGrouped[status], goal]
          });
        });
    })
  }

  showGoal(goal: Goal) {
    this.goal$.next(goal);
  }

  closeGoal() {
    this.goal$.next(null);
  }

  getCompletedTasks(goal: Goal) {
    return goal.tasks.filter(t => t.completed).length;
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

  stringToGoalStatus(status: string): GoalStatus {
    if (status === '') return GoalStatus.TODO;
    return GoalStatus[status as keyof typeof GoalStatus];
  }

  handleDrop($event: CdkDragDrop<Goal[]>) {
    if ($event.previousContainer.id === $event.container.id) return;

    // TODO: Si $event.container.id === 'DONE' && goal non terminé alors demander confirmation via modal

    transferArrayItem(
      $event.previousContainer.data,
      $event.container.data,
      $event.previousIndex,
      $event.currentIndex,
    );

    // Get the element dragged
    const goal = $event.container.data[$event.currentIndex];

    // Update the goal status
    this.goalService.updateStatus(goal.goal_id, $event.container.id as GoalStatus)
      .pipe(take(1))
      .subscribe(() => {
        goal.status = this.stringToGoalStatus($event.container.id);
        this.alertService.show(
          "Le statut de la tâche a été mis à jour",
          { type: AlertType.SUCCESS, duration: 5000 }
        );
      })
  }

  deadlineIsSoon(deadline: Date) {
    // TODO: Permettre de configurer le nombre de jours avant la date limite
    // Return true if the deadline is in less than 5 days
    return new Date(deadline).getTime() - new Date().getTime() < 5 * 24 * 60 * 60 * 1000;
  }

  updateGoal($event: Goal) {
    this.goalsGrouped$.next({
      [GoalStatus.TODO]: this.goalsGrouped[GoalStatus.TODO].filter(g => g.goal_id !== $event.goal_id) ?? [],
      [GoalStatus.IN_PROGRESS]: this.goalsGrouped[GoalStatus.IN_PROGRESS].filter(g => g.goal_id !== $event.goal_id) ?? [],
      [GoalStatus.DONE]: this.goalsGrouped[GoalStatus.DONE].filter(g => g.goal_id !== $event.goal_id) ?? []
    })
    this.goalsGrouped$.next({
      ...this.goalsGrouped,
      [$event.status]: [...this.goalsGrouped[$event.status], $event]
    });
  }

  deleteGoal($event: number) {
    console.log($event);
    this.goalsGrouped$.next({
      [GoalStatus.TODO]: this.goalsGrouped[GoalStatus.TODO].filter(g => g.goal_id !== $event) ?? [],
      [GoalStatus.IN_PROGRESS]: this.goalsGrouped[GoalStatus.IN_PROGRESS].filter(g => g.goal_id !== $event) ?? [],
      [GoalStatus.DONE]: this.goalsGrouped[GoalStatus.DONE].filter(g => g.goal_id !== $event) ?? []
    })
  }
}
