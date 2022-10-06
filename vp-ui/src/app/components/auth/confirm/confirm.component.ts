import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../services/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {

  formGroup = this.fb.group({
    code: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  confirm() {
    if (!this.formGroup.controls['code'].value) return;
    if (this.formGroup.valid) {
      const remember = this.route.snapshot.queryParams['remember'];
      this.authService.validateCode(this.formGroup.controls["code"].value).then(async () => {
        await this.router.navigate(['/login'], {queryParams: {remember}});
      }).catch(err => {
        console.error(err);
      });
    }
  }

  resend() {
    this.authService.resendValidationCode().then(() => {
      console.log("Code resent");
    }).catch(err => {
      console.error(err);
    });
  }

  ngOnInit(): void {
  }

}
