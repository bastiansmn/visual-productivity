import { Injectable } from '@angular/core';
import Goal, {GoalCreation, GoalStatus} from "../../model/goal.model";
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs";
import {handleError} from "../../utils/http-error-handler.util";
import {LoaderService} from "../loader/loader.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
  providedIn: 'root'
})
export class GoalService {

  constructor(
    private http: HttpClient,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  addGoalInProject(goal: GoalCreation) {
    return this.http.post<Goal>("/api/v1/goal/create", goal)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      );
  }

  updateStatus(goalID: number, status: GoalStatus) {
    return this.http.patch<Goal>(`/api/v1/goal/updateStatus`, {
      goal_id: goalID,
      goalStatus: status
    })
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      );
  }

  deleteGoal(goal_id: number) {
    return this.http.delete(`/api/v1/goal/delete?goal_id=${goal_id}`)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService
        }))
      );
  }
}
