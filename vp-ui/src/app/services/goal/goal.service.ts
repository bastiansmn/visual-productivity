import {Injectable} from '@angular/core';
import Goal, {GoalCreation, GoalStatus} from "../../model/goal.model";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GoalService {

  constructor(
    private http: HttpClient
  ) { }

  addGoalInProject(goal: GoalCreation) {
    return this.http.post<Goal>("/api/v1/goal/create", goal)
  }

  updateStatus(goalID: number, status: GoalStatus) {
    return this.http.patch<Goal>(`/api/v1/goal/updateStatus`, {
      goal_id: goalID,
      goalStatus: status
    })
  }

  deleteGoal(goal_id: number) {
    return this.http.delete(`/api/v1/goal/delete?goal_id=${goal_id}`)
  }
}
