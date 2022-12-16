import {AfterViewInit, Component, OnDestroy} from '@angular/core';
import {AuthService} from "../../../services/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";
import {Subject, takeUntil} from "rxjs";
import {LoaderService} from "../../../services/loader/loader.service";
import {AlertService, AlertType} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnDestroy, AfterViewInit {

  componentDestroyed$ = new Subject<boolean>();
  formGroup = this.fb.group({
    code: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(8)]]
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  confirm() {
    if (!this.formGroup.controls['code'].value) return;
    if (this.formGroup.valid) {
      const remember = this.route.snapshot.queryParams['remember'];
      this.loaderService.show();
      this.authService.confirmMail(this.formGroup.controls["code"].value)
        .pipe(
          takeUntil(this.componentDestroyed$)
        )
        .subscribe(async () => {
          this.loaderService.hide();
          this.alertService.show(
            "Votre compte a été validé",
            { duration: 5000, type: AlertType.SUCCESS }
          );
          await this.router.navigate(['/'], {queryParams: { remember }});
        })
    }
  }

  resend() {
    this.loaderService.show();
    this.authService.revalidateEmail()
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(() => {
        this.loaderService.hide();
        this.alertService.show(
          "Vous allez recevoir un mail de confirmation",
          { type: AlertType.INFO, duration: 5000 }
        );
      });
  }

  ngAfterViewInit(): void {
    (document.querySelector('input#code') as HTMLInputElement)?.focus();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
