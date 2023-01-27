import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../model/project.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GoalStatus} from "../../../../../model/goal.model";
import {dateIsAfterToday} from "../../../../../validators/date-is-valid.validator";
import {isBeforeDeadline} from "../../../../../validators/date-start-before-deadline.validator";
import {isAfterDateStart} from "../../../../../validators/deadline-after-date-start.validator";
import {validateStatus} from "../../../../../validators/validate-status.validator";

@Component({
  selector: 'app-add-goal-dialog',
  templateUrl: './add-goal-dialog.component.html',
  styleUrls: ['./add-goal-dialog.component.scss']
})
export class AddGoalDialogComponent implements OnInit {

  project!: Project;
  form!: FormGroup;

  options = [
    {value: GoalStatus.TODO, label: 'A faire'},
    {value: GoalStatus.IN_PROGRESS, label: 'En cours'}
  ]

  constructor(
    public dialogRef: MatDialogRef<AddGoalDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { project: Project, type: GoalStatus },
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.project = this.data.project;
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'name':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un nom"
        return "Une erreur inconnue est survenue";
      case 'description':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une description"
        return "Une erreur inconnue est survenue";
      case 'date_start':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de début"
        if (this.form.controls[formControlName].errors?.['isAfter'])
          return "La date de début doit être après la date actuelle"
        if (this.form.controls[formControlName].errors?.['dateValidation'])
          return "La date de début doit être avant la date de fin"
        if (this.form.controls[formControlName].errors?.['setDates'])
          return "Veuillez saisir une date de début et une date de fin"
        return "Une erreur inconnue est survenue";
      case 'deadline':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        if (this.form.controls[formControlName].errors?.['dateValidation'])
          return "La date de fin doit être après la date de début"
        if (this.form.controls[formControlName].errors?.['isAfter'])
          return "La date de fin doit être après la date actuelle"
        if (this.form.controls[formControlName].errors?.['setDates'])
          return "Veuillez saisir une date de début et une date de fin"
        if (this.form.controls[formControlName].errors?.['projectDateEnd'])
          return `La date de fin doit être avant la date de fin du projet (${new Date(this.project?.deadline).toLocaleDateString()})`
        return "Une erreur inconnue est survenue";
      case 'goalStatus':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un statut"
        if (this.form.controls[formControlName].errors?.['validateStatus'])
          return "Le statut doit être valide"
        return "Une erreur inconnue est survenue";
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  initForm() {
    this.form = this.fb.group({
      name: this.fb.control('', [Validators.required]),
      description: this.fb.control('', [Validators.required]),
      date_start: this.fb.control<Date | null>(null, [Validators.required, dateIsAfterToday]),
      goalStatus: this.fb.control<GoalStatus | null>(this.data.type, [Validators.required, validateStatus]),
      deadline: this.fb.control<Date | null>(null, [Validators.required, dateIsAfterToday, this.beforeProjectEnd.bind(this)]),
    }, {validators: [isBeforeDeadline, isAfterDateStart]});
  }

  private beforeProjectEnd(control: any) {
    const date = new Date(control.value);
    const projectDateEnd = new Date(this.project?.deadline);
    return date.getTime() <= projectDateEnd.getTime() ? null : {projectDateEnd: true};
  }

  close() {
    if (this.form.invalid) return this.dialogRef.close();
    this.form.setValidators(null);
    const goal = this.form.value;
    this.dialogRef.close({
      ...goal,
      project_id: this.project.projectId
    });
  }

}
