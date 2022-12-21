import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../model/project.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-add-user-dialog',
  templateUrl: './add-user-dialog.component.html',
  styleUrls: ['./add-user-dialog.component.scss']
})
export class AddUserDialogComponent implements OnInit {

  project!: Project;

  form!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<AddUserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Project,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.project = this.data;
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'email':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un email"
        if (this.form.controls[formControlName].errors?.['pattern'])
          return "Veuillez saisir un email valide"
        return "Une erreur inconue est survenue";
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  initForm() {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]]
    })
  }

  close() {
    this.form.setValidators(null);
    this.dialogRef.close(this.form.get('email')?.value);
  }

}
