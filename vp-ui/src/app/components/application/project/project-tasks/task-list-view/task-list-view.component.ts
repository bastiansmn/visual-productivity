import {Component, Input} from '@angular/core';
import Project from "../../../../../model/project.model";

@Component({
  selector: 'app-task-list-view',
  templateUrl: './task-list-view.component.html',
  styleUrls: ['./task-list-view.component.scss']
})
export class TaskListViewComponent {

  @Input() project!: Project | null;

  trackByFn = (item: any) => item.id;

}
