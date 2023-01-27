import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../model/project.model";

@Component({
  selector: 'app-add-label-dialog',
  templateUrl: './add-label-dialog.component.html',
  styleUrls: ['./add-label-dialog.component.scss']
})
export class AddLabelDialogComponent implements OnInit {

  form!: FormGroup;

  constructor(
    private _fb: FormBuilder,
    public dialogRef: MatDialogRef<AddLabelDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Project,
  ) { }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'label':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un nom de label";
        return "Une erreur inconnue est survenue";
      case 'color':
        if (this.form.controls[formControlName].errors?.['required'])
          return "Veuillez saisir une couleur";
        return "Une erreur inconnue est survenue";
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  ngOnInit(): void {
    this.form = this._fb.group({
      name: this._fb.control('', Validators.required),
      color: this._fb.control("#7d70ff", Validators.required)
    });
  }

  close() {
    if (this.form.invalid) return this.dialogRef.close();
    this.dialogRef.close(this.form.value);
  }
}
