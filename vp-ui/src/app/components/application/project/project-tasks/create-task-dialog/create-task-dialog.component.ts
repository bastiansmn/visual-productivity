import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../../../model/project.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import Goal from "../../../../../model/goal.model";

@Component({
  selector: 'app-create-task-dialog',
  templateUrl: './create-task-dialog.component.html',
  styleUrls: ['./create-task-dialog.component.scss']
})
export class CreateTaskDialogComponent implements OnInit {

  form!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<CreateTaskDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Project,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      description: this.fb.control<string>(''),
      date_start: this.fb.control<Date | null>(null, [Validators.required]),
      date_end: this.fb.control<Date | null>(null, [Validators.required]),
      goal: this.fb.control<Goal | null>(null),
    });
  }

  close() {
    if (this.form.invalid) return this.dialogRef.close();
    this.form.setValidators(null);
    this.dialogRef.close({
      ...this.form.value,
      project_id: this.data.projectId,
      goal_id: this.form.value.goal?.id
    });
  }

}
