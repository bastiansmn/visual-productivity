import {Component, EventEmitter, Input, Output} from '@angular/core';
import Goal from "../../../../../model/goal.model";

@Component({
  selector: 'app-edit-goal',
  templateUrl: './edit-goal.component.html',
  styleUrls: ['./edit-goal.component.scss']
})
export class EditGoalComponent {

  @Input() goal!: Goal | null;
  @Output() closed = new EventEmitter();

  getCompletedTasks() {
    return this.goal?.tasks.filter(t => t.completed).length;
  }

  close() {
    this.closed.emit();
  }

}
