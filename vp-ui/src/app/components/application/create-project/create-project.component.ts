import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {dateIsAfterToday} from "../../../validators/date-is-valid.validator";
import {ProjectService} from "../../../services/project/project.service";
import {Subject, takeUntil} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.scss']
})
export class CreateProjectComponent implements OnInit, OnDestroy {

  form!: FormGroup;
  private componentDestroyed$ = new Subject<boolean>();

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private router: Router
  ) { }

  private initForm(): void {
    this.form = this.fb.group({
      name: this.fb.control('', Validators.required),
      description: this.fb.control('', Validators.required),
      deadline: this.fb.control<Date | null>(null, [Validators.required, dateIsAfterToday]),
      complete_mode: this.fb.control(false)
    });
  }

  createProject() {
    this.projectService.createProject(this.form.value)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(project => {
        this.projectService.projects.push(project);
        this.router.navigate(['/app/projects/' + project.projectId]);
      });
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'name':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un nom"
        return "Une erreur inconnue est survenue"
      case 'description':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une description"
        return "Une erreur inconnue est survenue"
      case 'deadline':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        if (this.form.controls[formControlName].errors?.['isAfter'])
          return "La date de fin doit être supérieure à la date d'aujourd'hui"
        return "Une erreur inconnue est survenue"
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  ngOnInit(): void {
    this.initForm();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
