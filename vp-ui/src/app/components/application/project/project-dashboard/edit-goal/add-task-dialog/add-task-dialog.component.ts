import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../../model/project.model";
import Goal from "../../../../../../model/goal.model";
import {dateIsAfterToday} from "../../../../../../validators/date-is-valid.validator";
import {isBeforeDateEnd} from "../../../../../../validators/date-start-before-date-end.validator";
import {isAfterDateStartDateEnd} from "../../../../../../validators/date-end-after-date-start.validator";

@Component({
  selector: 'app-add-task-dialog',
  templateUrl: './add-task-dialog.component.html',
  styleUrls: ['./add-task-dialog.component.scss']
})
export class AddTaskDialogComponent implements OnInit {

  form!: FormGroup
  project!: Project;
  goal!: Goal;

  constructor(
    public dialogRef: MatDialogRef<AddTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { project: Project, goal: Goal },
    private fb: FormBuilder
  ) {
    this.project = data.project;
    this.goal = data.goal;
  }

  private initForm() {
    this.form = this.fb.group({
      name: this.fb.control('', [Validators.required]),
      description: this.fb.control('', [Validators.required]),
      date_start: this.fb.control<Date | null>(null, [Validators.required, dateIsAfterToday, this.isAfterGoalStart.bind(this)]),
      date_end: this.fb.control<Date | null>(null, [Validators.required, dateIsAfterToday, this.beforeGoalEnd.bind(this)]),
    }, {validators: [isBeforeDateEnd, isAfterDateStartDateEnd]})
  }

  private isAfterGoalStart(control: any) {
    const date = new Date(control.value);
    const goalDateStart = new Date(this.goal.date_start);
    return date.getTime() >= goalDateStart.getTime() ? null : {goalDateStart: true};
  }

  private beforeGoalEnd(control: any) {
    const date = new Date(control.value);
    const goalDateEnd = new Date(this.goal.deadline);
    return date.getTime() <= goalDateEnd.getTime() ? null : {goalDateEnd: true};
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
        if (this.form.controls[formControlName].errors?.['goalDateStart'])
          return `La date de début doit être après la date de début de l'objectif (${new Date(this.goal.date_start).toLocaleDateString()})`
        return "Une erreur inconnue est survenue";
      case 'date_end':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        if (this.form.controls[formControlName].errors?.['dateValidation'])
          return "La date de fin doit être après la date de début"
        if (this.form.controls[formControlName].errors?.['isAfter'])
          return "La date de fin doit être après la date actuelle"
        if (this.form.controls[formControlName].errors?.['setDates'])
          return "Veuillez saisir une date de début et une date de fin"
        if (this.form.controls[formControlName].errors?.['goalDateEnd'])
          return `La date de fin doit être avant la date de fin de l'objectif (${new Date(this.goal.deadline).toLocaleDateString()})`
        return "Une erreur inconnue est survenue";
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  close() {
    if (this.form.invalid) return this.dialogRef.close();

    this.dialogRef.close({
      ...this.form.value,
      goal_id: this.goal.goal_id,
      project_id: this.project.projectId
    });
  }

  ngOnInit(): void {
    this.initForm();
  }

}
