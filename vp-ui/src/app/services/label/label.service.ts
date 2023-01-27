import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EMPTY, Observable} from "rxjs";
import Label, {LabelAssignation, LabelCreation} from "../../model/label.model";
import Goal from "../../model/goal.model";

@Injectable({
  providedIn: 'root'
})
export class LabelService {

  constructor(
    private http: HttpClient
  ) { }

  getLabelByName(params: { project_id: string, name: string }): Observable<Label[]> {
    return this.http.get<Label[]>('/api/v1/label/filterByName', {params});
  }

  assignLabel(params: LabelAssignation): Observable<Goal> {
    return this.http.put<Goal>('/api/v1/goal/assignLabel', params);
  }

  unassignLabel(params: LabelAssignation): Observable<Goal> {
    return this.http.delete<Goal>('/api/v1/goal/unassignLabel', {
      body: params
    });
  }

  createLabel(params: LabelCreation): Observable<Label> {
    return this.http.post<Label>('/api/v1/label/create', params);
  }

  deleteLabel(label_id: number): Observable<null> {
    if (!label_id) return EMPTY;
    return this.http.delete<null>(`/api/v1/label/delete?label_id=${label_id}`);
  }

  labelOfProject(project_id: string): Observable<Label[]> {
    if (!project_id) return EMPTY;
    return this.http.get<Label[]>(`/api/v1/label/allOfProject?project_id=${project_id}`);
  }
}
