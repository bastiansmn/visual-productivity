import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, EMPTY, from, Observable, of} from "rxjs";
import {Error} from "../../model/error.model";
import {environment} from "../../../environments/environment";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import Project from "../../model/project.model";
import {handleError} from "../../http-error-handler.util";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  projects$ = new BehaviorSubject<Project[]>([]);

  get projects() {
    return this.projects$.getValue();
  }

  constructor(
    private http: HttpClient,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  fetchProjectById(_id: string) {
    const optProject = this.projects.find(p => p.project_id === _id);
    if (optProject)
      return of(optProject);
    return this.http.get<Project>(`/api/v1/project/fetchById?project_id=${_id}`)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  fetchProjects() {
    return this.http.get<Project[]>("/api/v1/project/myProjects")
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  handleError(err: HttpErrorResponse) {
    const error = err.error as Error;
    if (!environment.production) {
      console.error(error.devMessage);
      console.error(error.httpStatus, error.httpStatusString);
    }
    this.alertService.show(
      error.message,
      { duration: 5000, type: AlertType.ERROR }
    )
    this.loaderService.hide();
    return EMPTY;
  }

}
