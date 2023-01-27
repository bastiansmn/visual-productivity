import {Component, EventEmitter, Input, Output} from '@angular/core';
import Task from "../../../../../model/task.model";
import {TaskService} from "../../../../../services/task/task.service";
import {take} from "rxjs";
import {AlertService, AlertType} from "../../../../../services/alert/alert.service";
import Goal, {GoalStatus} from "../../../../../model/goal.model";

@Component({
  selector: 'app-task-preview',
  templateUrl: './task-preview.component.html',
  styleUrls: ['./task-preview.component.scss']
})
export class TaskPreviewComponent {

  @Input() task!: Task;
  @Input() goal!: Goal | null;
  @Output() deleted = new EventEmitter();
  @Output() done = new EventEmitter();
  @Output() statusChanged = new EventEmitter();

  GoalStatus = GoalStatus;

  constructor(
    private taskService: TaskService,
    private alertService: AlertService
  ) { }

  delete() {
    this.taskService.deleteTask(this.task.task_id)
      .pipe(take(1))
      .subscribe(() => {
        this.deleted.emit(this.task.task_id);
      })
  }

  changeCompletedStatus() {
    if (this.task.completed) {
      this.taskService.markAsUndone(this.task.task_id)
        .pipe(take(1))
        .subscribe(() => {
          this.statusChanged.emit();
          this.task.completed = false;
        });
    } else {
      this.taskService.markAsDone(this.task.task_id)
        .pipe(take(1))
        .subscribe(() => {
          this.task.completed = true;
          this.statusChanged.emit();
          this.done.emit();
          this.alertService.show(
            "Tâche complétée",
            { type: AlertType.SUCCESS, duration: 5000 }
          );
        });
    }
  }

  dateStartPassed() {
    return this.goal?.status !== GoalStatus.DONE && new Date(this.task.date_start) <= new Date();
  }
}
