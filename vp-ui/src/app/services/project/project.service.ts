import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, of} from "rxjs";
import Project, {ProjectCreation} from "../../model/project.model";
import {User} from "../../model/user.model";

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  projects$ = new BehaviorSubject<Project[]>([]);

  get projects() {
    return this.projects$.getValue();
  }

  constructor(
    private http: HttpClient
  ) { }

  fetchProjectById(_id: string) {
    const optProject = this.projects.find(p => p.projectId === _id);
    if (optProject)
      return of(optProject);
    return this.http.get<Project>(`/api/v1/project/fetchById?project_id=${_id}`)
  }

  fetchProjects() {
    return this.http.get<Project[]>("/api/v1/project/myProjects")
  }

  createProject(project: ProjectCreation) {
    return this.http.post<Project>("/api/v1/project/create", project)
  }

  addUserInProject(email: string, projectId: string) {
    return this.http.post<User | null>(`/api/v1/project/addUserToProject?user_email=${email}&project_id=${projectId}`, {})
  }

}
