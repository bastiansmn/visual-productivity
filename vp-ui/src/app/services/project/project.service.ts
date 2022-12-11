import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, EMPTY, Observable} from "rxjs";
import {Error} from "../../model/error.model";
import {environment} from "../../../environments/environment";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import Project from "../../model/project.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  private projects: Project[] = [];

  get getProjects() {
    return this.projects;
  }

  constructor(
    private http: HttpClient,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  fetchProjects() {
    this.loaderService.show();
    return this.http.get<Project[]>("/api/v1/project/myProjects")
      .pipe(
        catchError((err: HttpErrorResponse) => {
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
          return EMPTY
        })
      )
      .subscribe((projects: Project[]) => {
        this.projects = projects;
        this.loaderService.hide();
      });
  }

}
