import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Task, {TaskCreation} from "../../model/task.model";
import {catchError} from "rxjs";
import {handleError} from "../../utils/http-error-handler.util";
import {LoaderService} from "../loader/loader.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
    private http: HttpClient,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  createTask(task: TaskCreation) {
    return this.http.post<Task>("/api/v1/task/create", task)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      );
  }

  markAsDone(taskID: number) {
    return this.http.patch(`/api/v1/task/markAsDone?task_id=${taskID}`, {})
  }

  markAsUndone(taskID: number) {
    return this.http.patch(`/api/v1/task/markAsUndone?task_id=${taskID}`, {})
  }

  deleteTask(taskID: number) {
    return this.http.delete(`/api/v1/task/delete?task_id=${taskID}`)
  }

}
