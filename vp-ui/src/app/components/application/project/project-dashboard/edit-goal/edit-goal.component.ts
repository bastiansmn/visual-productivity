import {Component, EventEmitter, Input, Output} from '@angular/core';
import Goal from "../../../../../model/goal.model";
import {MatDialog} from "@angular/material/dialog";
import {AddTaskDialogComponent} from "./add-task-dialog/add-task-dialog.component";
import Project from "../../../../../model/project.model";
import {take} from "rxjs";

@Component({
  selector: 'app-edit-goal',
  templateUrl: './edit-goal.component.html',
  styleUrls: ['./edit-goal.component.scss']
})
export class EditGoalComponent {

  @Input() goal!: Goal | null;
  @Input() project!: Project | null;
  @Output() closed = new EventEmitter();

  constructor(
    private dialog: MatDialog
  ) {  }

  getCompletedTasks() {
    return this.goal?.tasks.filter(t => t.completed).length;
  }

  close() {
    this.closed.emit();
  }

  openAddTaskDialog() {
    const dialogRef = this.dialog.open(AddTaskDialogComponent, {
      data: {
        goal: this.goal,
        project: this.project
      },
      disableClose: true
    });

    dialogRef.afterClosed().pipe(take(1)).subscribe(() => {
      this.goal = null;
      this.closed.emit();
    });
  }
}
