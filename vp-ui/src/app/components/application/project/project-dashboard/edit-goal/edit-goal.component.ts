import {Component, EventEmitter, Input, Output} from '@angular/core';
import Goal, {GoalStatus} from "../../../../../model/goal.model";
import {MatDialog} from "@angular/material/dialog";
import {AddTaskDialogComponent} from "./add-task-dialog/add-task-dialog.component";
import Project from "../../../../../model/project.model";
import {take} from "rxjs";
import {TaskService} from "../../../../../services/task/task.service";
import Task from "../../../../../model/task.model";
import {AlertService, AlertType} from "../../../../../services/alert/alert.service";

@Component({
  selector: 'app-edit-goal',
  templateUrl: './edit-goal.component.html',
  styleUrls: ['./edit-goal.component.scss']
})
export class EditGoalComponent {

  @Input() goal!: Goal | null;
  @Input() project!: Project | null;
  @Output() closed = new EventEmitter();

  GoalStatus = GoalStatus;

  constructor(
    private dialog: MatDialog,
    private taskService: TaskService,
    private alertService: AlertService
  ) {  }

  getCompletedTasks() {
    return this.goal?.tasks.filter(t => t.completed).length;
  }

  close() {
    this.closed.emit();
  }

  openAddTaskDialog() {
    if (this.dialog.openDialogs.length > 0) return;

    const dialogRef = this.dialog.open(AddTaskDialogComponent, {
      data: {
        project: this.project,
        goal: this.goal
      },
      disableClose: true
    });

    dialogRef.afterClosed().pipe(take(1)).subscribe(payload => {
      if (!payload) return;

      this.taskService.createTask(payload)
        .pipe(take(1))
        .subscribe(task => {
          this.goal?.tasks.push(task);
        });
    });
  }

  handleDeletedTask($event: number) {
    if (!this.goal) return;
    this.goal.tasks = this.goal?.tasks.filter(t => t.task_id !== $event);
    this.alertService.show(
      "Tâches supprimée",
      { type: AlertType.WARNING, duration: 3000 }
    )
  }

  getSortedTasks(tasks: Task[] | undefined) {
    if (!tasks) return [];
    return tasks.sort((a, b) => {
      if (a.completed && !b.completed) return -1;
      if (!a.completed && b.completed) return 1;

      if (a.date_start === b.date_start) {
        return a.date_end > b.date_end ? 1 : -1;
      }

      return a.date_start > b.date_start ? 1 : -1;
    });
  }

  resort() {
    if (!this.goal) return;
    this.goal.tasks = this.getSortedTasks(this.goal.tasks);
  }

  deadlineIsSoon(deadline: Date | undefined) {
    if (!deadline) return false;
    // TODO: Permettre de configurer le nombre de jours avant la date limite
    // Return true if the deadline is in less than 5 days
    return new Date(deadline).getTime() - new Date().getTime() < 5 * 24 * 60 * 60 * 1000;
  }
}
