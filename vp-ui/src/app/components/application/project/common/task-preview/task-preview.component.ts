import {Component, EventEmitter, Input, Output} from '@angular/core';
import Task from "../../../../../model/task.model";
import {TaskService} from "../../../../../services/task/task.service";
import {take} from "rxjs";
import {AlertService, AlertType} from "../../../../../services/alert/alert.service";

@Component({
  selector: 'app-task-preview',
  templateUrl: './task-preview.component.html',
  styleUrls: ['./task-preview.component.scss']
})
export class TaskPreviewComponent {

  @Input() task!: Task;
  @Output() deleted = new EventEmitter();
  @Output() done = new EventEmitter();

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
          this.task.completed = false;
        });
    } else {
      this.taskService.markAsDone(this.task.task_id)
        .pipe(take(1))
        .subscribe(() => {
          this.task.completed = true;
          this.done.emit();
          this.alertService.show(
            "Tâche complétée",
            { type: AlertType.SUCCESS, duration: 5000 }
          );
        });
    }
  }
}
