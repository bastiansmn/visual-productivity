import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../model/project.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import Goal from "../../../../../model/goal.model";
import {markAllAsDirty} from "../../../../../utils/form.utils";

@Component({
  selector: 'app-create-task-dialog',
  templateUrl: './create-task-dialog.component.html',
  styleUrls: ['./create-task-dialog.component.scss']
})
export class CreateTaskDialogComponent implements OnInit {

  form!: FormGroup;

  get goals() {
    return this.data.allGoals.map(goal => ({
      value: goal,
      label: goal.name
    }));
  }

  hasDurationErrors() {
    return (this.form.controls['duration'].dirty || this.form.controls['date_start'].dirty || this.form.controls['date_end'].dirty)
      && !this.form.controls['duration'].value && !this.form.controls['date_start'].value && !this.form.controls['date_end'].value;
  }

  constructor(
    public dialogRef: MatDialogRef<CreateTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Project,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string | null>(null, [Validators.required]),
      description: this.fb.control<string | null>(null),
      date_start: this.fb.control<Date | null>(null),
      date_end: this.fb.control<Date | null>(null),
      duration: this.fb.control<number | null>(null),
      goal: this.fb.control<Goal | null>(null),
    }, { validators: this.dateOrDurationValidator });
  }

  close() {
    this.dialogRef.close();
  }

  save() {
    if (this.form.invalid) {
      markAllAsDirty(this.form);
      return;
    }

    this.form.setValidators(null);
    this.dialogRef.close({
      ...this.form.value,
      project_id: this.data.projectId,
      goal_id: this.form.value.goal?.id
    });
  }

  getErrorMessage(formControlName: string) {
    switch (formControlName) {
      case 'name':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un nom"
        return "Une erreur inconnue est survenue"
      case 'date_start':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        return "Une erreur inconnue est survenue"
      case 'date_end':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une date de fin"
        return "Une erreur inconnue est survenue"
      case 'project':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez sélectionner un projet"
        return "Une erreur inconnue est survenue"
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  private dateOrDurationValidator() {
    return (group: FormGroup) => {
      const dateStart = group.controls['date_start'];
      const dateStartV = dateStart.value;
      const dateEnd = group.controls['date_end'];
      const dateEndV = dateEnd.value;
      const duration = group.controls['duration'];
      const durationV = duration.value;

      // If duration isn't set, dateStart and dateEnd must be set
      if (!durationV) {
        if (!dateStartV || !dateEndV) {
          dateStart.setErrors({ required: true });
          dateEnd.setErrors({ required: true });
          return { dateOrDuration: true };
        }
      }

      return null;
    }
  }
}
