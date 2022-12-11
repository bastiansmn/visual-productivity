import {Component, OnInit} from '@angular/core';
import {ProjectService} from "../../services/project/project.service";
import Project from "../../model/project.model";

@Component({
  selector: 'app-application',
  templateUrl: './application.component.html',
  styleUrls: ['./application.component.scss', '_application-theme.component.scss']
})
export class ApplicationComponent implements OnInit {

  navbarToggled = false;

  get projects(): Project[] {
    return this.projectService.getProjects;
  }

  constructor(
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.projectService.fetchProjects()
  }

  toggleNavigation(): void {
    this.navbarToggled = !this.navbarToggled;
  }

}
