import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ProjectService} from "../../../../services/project/project.service";
import {BehaviorSubject, Subject, take, takeUntil} from "rxjs";
import Project from "../../../../model/project.model";
import {MatDialog} from "@angular/material/dialog";
import {TaskService} from "../../../../services/task/task.service";
import {CreateTaskDialogComponent} from "./create-task-dialog/create-task-dialog.component";

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

  switcherOptions = [
    { icon: 'dns', value: 'list-view', tooltip: 'Vue liste' },
    { icon: 'view_quilt', value: 'timeline-view', tooltip: 'Vue timeline' }
  ]
  selectedView = 'list-view';

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private taskService: TaskService,
    private dialog: MatDialog,
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

  handleViewSwitch($event: { icon: string; value: string }) {
    this.selectedView = $event.value;
  }

  toggleCreateTaskDialog() {
    const dialogRef = this.dialog.open(CreateTaskDialogComponent, {
      data: this.project,
      disableClose: true
    });

    dialogRef.afterClosed().pipe(take(1)).subscribe(result => {
      if (!this.project?.projectId) return;

      console.log(result);

      this.taskService.createTask(result);
    });
  }
}
