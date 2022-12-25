import { Injectable } from '@angular/core';
import Goal, {GoalCreation} from "../../model/goal.model";
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

}
