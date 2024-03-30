import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import Project from "../../../../../model/project.model";
import {ProjectService} from "../../../../../services/project/project.service";
import {take} from "rxjs";

@Component({
  selector: 'app-create-event-dialog',
  templateUrl: './create-event-dialog.component.html',
  styleUrls: ['./create-event-dialog.component.scss']
})
export class CreateEventDialogComponent implements OnInit {

  eventForm!: FormGroup
  projects!: Project[];

  constructor(
    private dialogRef: MatDialogRef<CreateEventDialogComponent>,
    private fb: FormBuilder,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {
    this.projectService.fetchProjects()
      .pipe(take(1))
      .subscribe(projects => {
        this.projects = projects;
      });
    this.eventForm = this.fb.group({
      name: this.fb.control("", [Validators.required]),
      description: this.fb.control(""),
      videoCallLink: this.fb.control(""),
      project: this.fb.control<Project | null>(null, [Validators.required]),
    });
  }

  createEvent() {
    this.dialogRef.close(this.eventForm.value);
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'name':
        if (this.eventForm.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un nom"
        return "Une erreur inconnue est survenue"
      case 'date_start':
        if (this.eventForm.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        return "Une erreur inconnue est survenue"
      case 'date_end':
        if (this.eventForm.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        return "Une erreur inconnue est survenue"
      case 'project':
        if (this.eventForm.controls[formControlName].errors?.['required'])
          return "Veuillez sélectionner un projet"
        return "Une erreur inconnue est survenue"
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  close() {
    if (this.eventForm.invalid) return this.dialogRef.close();

    this.createEvent();
  }

  get projectsMapped() {
    return this.projects?.map(p => ({value: p.projectId, label: p.name})) ?? [];
  }
}
