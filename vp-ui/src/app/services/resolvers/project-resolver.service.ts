import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import Project from "../../model/project.model";
import {catchError, EMPTY, Observable} from "rxjs";
import {ProjectService} from "../project/project.service";

@Injectable({
  providedIn: 'root'
})
export class ProjectResolverService implements Resolve<Project[]>{

  constructor(
    private projectService: ProjectService
  ) { }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Project[]> | Promise<Project[]> | Project[] {
    return this.projectService.fetchProjects()
  }
}
