import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Task, {TaskCreation} from "../../model/task.model";

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(
    private http: HttpClient
  ) { }

  createTask(task: TaskCreation) {
    return this.http.post<Task>("/api/v1/task/create", task)
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
