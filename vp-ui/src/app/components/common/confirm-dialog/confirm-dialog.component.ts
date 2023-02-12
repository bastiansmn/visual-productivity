import {Component, Inject, Input} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import Project from "../../../model/project.model";
import {GoalStatus} from "../../../model/goal.model";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title: string,
      message: string,
      cancelText: string,
      confirmText: string
    }
  ) { }

  public close(value: boolean) {
    this.dialogRef.close(value);
  }

  public cancel() {
    this.close(false);
  }

  public confirm() {
    this.close(true);
  }

}
