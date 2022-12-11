import {Component, OnInit} from '@angular/core';
import Project from "../../../model/project.model";
import {ProjectService} from "../../../services/project/project.service";

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent implements OnInit {

  private project?: Project;

  get getProject() {
    return this.project;
  }

  constructor(
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    console.log(this.projectService.getProjects);
  }

}
