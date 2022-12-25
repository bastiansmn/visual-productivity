import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, EMPTY, from, Observable, of} from "rxjs";
import {Error} from "../../model/error.model";
import {environment} from "../../../environments/environment";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import Project, {ProjectCreation} from "../../model/project.model";
import {handleError} from "../../utils/http-error-handler.util";
import {User} from "../../model/user.model";
import {GoalCreation} from "../../model/goal.model";

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
    const optProject = this.projects.find(p => p.projectId === _id);
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

  createProject(project: ProjectCreation) {
    return this.http.post<Project>("/api/v1/project/create", project)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  addUserInProject(email: string, projectId: string) {
    return this.http.post<User>(`/api/v1/project/addUserToProject?user_email=${email}&project_id=${projectId}`, {})
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        })
      ));
  }

}
