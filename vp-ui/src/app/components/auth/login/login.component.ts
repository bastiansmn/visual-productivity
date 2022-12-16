import {Component, OnDestroy, OnInit} from '@angular/core';
import {SocialAuthService, SocialUser} from "@abacritt/angularx-social-login";
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth/auth.service";
import {User, UserLogin} from "../../../model/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {LoaderService} from "../../../services/loader/loader.service";
import {AlertService, AlertType} from "../../../services/alert/alert.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  readonly SUCCESS_REDIRECT_ROUTE = '/app';
  componentDestroyed$ = new Subject<boolean>();

  formGroup = this.fb.group({
    email: this.fb.control<string>('', [Validators.required, Validators.pattern("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")]),
    password: this.fb.control<string>('', Validators.required),
    remember: this.fb.control<boolean>(false)
  });

  constructor(
    private socialAuthService: SocialAuthService,
    private loaderService: LoaderService,
    private alertService: AlertService,
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  loginWithVP(): void {
    // Validate form
    this.formGroup.markAllAsTouched();

    if (this.formGroup.invalid) return;

    this.loaderService.show();
    this.authService.loginWithVP(this.formGroup.value as UserLogin)
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(async user => {
        this.loaderService.hide();
        this.alertService.show(
          "Connecté",
          { duration: 3000, type: AlertType.SUCCESS }
        )
        this.authService.isLoggedIn.next(true);
        this.authService.loggedUser.next(user);
        await this.router.navigate([this.SUCCESS_REDIRECT_ROUTE]);
      });
  }

  loginWithGoogle(user: SocialUser): void {
    this.authService.loginWithSocial(user)
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(async user => {
        console.log(user);
        this.alertService.show(
          "Connecté avec Google",
          { duration: 3000, type: AlertType.SUCCESS }
        )
        this.authService.isLoggedIn.next(true);
        this.authService.loggedUser.next(user);
        await this.router.navigate([this.SUCCESS_REDIRECT_ROUTE]);
      });
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'email':
        if (this.formGroup.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un email"
        if (this.formGroup.controls[formControlName].errors?.['pattern'])
          return "Email invalide"
        return "Une erreur inconnue est survenue"
      case 'password':
        if (this.formGroup.controls[formControlName].errors?.['required'])
          return "Veuillez saisir un mot de passe"
        return "Une erreur inconnue est survenue"
      default:
        return "Une erreur inconnue est survenue"
    }
  }

  ngOnInit(): void {
    this.socialAuthService.authState.subscribe((user: SocialUser) => {
      this.loginWithGoogle(user);
    });
    if (!!this.authService.loggedUser) {
      this.formGroup.controls['email'].setValue(<string>this.authService.loggedUser.getValue()?.email);
      this.formGroup.markAllAsTouched();
    }
    this.formGroup.controls['remember'].setValue(this.route.snapshot.queryParams['remember']);
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
