import {Component, OnInit} from '@angular/core';
import {Subject, take, takeUntil} from "rxjs";
import Project from "../../../../model/project.model";
import {LoaderService} from "../../../../services/loader/loader.service";
import {ProjectService} from "../../../../services/project/project.service";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {AddLabelDialogComponent} from "./add-label-dialog/add-label-dialog.component";
import {LabelService} from "../../../../services/label/label.service";

@Component({
  selector: 'app-project-settings',
  templateUrl: './project-settings.component.html',
  styleUrls: ['./project-settings.component.scss']
})
export class ProjectSettingsComponent implements OnInit {

  private componentDestroyed$ = new Subject<boolean>();

  project!: Project | undefined;

  constructor(
    private _loaderService: LoaderService,
    private _projectService: ProjectService,
    private _labelService: LabelService,
    private _route: ActivatedRoute,
    private matDialog: MatDialog
  ) { }

  ngOnInit(): void {
    this._loaderService.show();
    this._route.params
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(params => {
        this._loaderService.hide();
        this._projectService.projects$.pipe(takeUntil(this.componentDestroyed$))
          .subscribe(projects => {
            this._loaderService.hide();
            this.project = projects.find(p => p.projectId === params['id']) as Project;
          })
      });
  }

  toggleAddLabelDialog() {
    const dialogRef = this.matDialog.open(AddLabelDialogComponent);

    dialogRef.afterClosed()
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(result => {
        if (!result || !this.project?.projectId) return;

        this._labelService.createLabel({ ...result, projectId: this.project.projectId })
          .pipe(take(1))
          .subscribe(label => {
            this.project?.allLabels.push(label);
          });
      });
  }
}
