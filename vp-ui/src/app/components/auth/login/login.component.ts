import {AfterViewInit, Component, OnInit} from '@angular/core';
import {SocialAuthService, SocialUser} from "@abacritt/angularx-social-login";
import {FormBuilder, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth/auth.service";
import {LoginProvider} from "../../../model/user.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  formGroup = this.fb.group({
    email: ['', [Validators.required, Validators.pattern("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")]],
    password: ['', Validators.required],
    remember: [false]
  });

  constructor(
    private socialAuthService: SocialAuthService,
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  loginWithVP(): void {
    // Validate form
    const emailInput: HTMLInputElement | null = document.querySelector('input#email');
    const passwordInput: HTMLInputElement | null = document.querySelector('input#password');

    emailInput?.focus();
    emailInput?.blur();

    passwordInput?.focus();
    passwordInput?.blur();

    // If form is valid call auth service
    if (this.formGroup.valid) {
      this.authService.loginWithVP(
        {
          email: this.formGroup.controls['email'].value ?? '',
          password: this.formGroup.controls['password'].value ?? '',
          remember: this.formGroup.controls['remember'].value ?? false
        }
      ).then(async () => {
        await this.router.navigate(["/discover"])
      }).catch(err => {
        console.error(err);
      })
    }
  }

  loginWithGoogle(user: SocialUser): void {
    this.authService.socialLogin(user, LoginProvider.GOOGLE);
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
      this.formGroup.controls['email'].setValue(<string>this.authService.loggedUser.email);
      const emailInput: HTMLInputElement | null = document.querySelector('input#email');

      emailInput?.focus();
      emailInput?.blur();
    }
    this.formGroup.controls['remember'].setValue(!!this.route.snapshot.queryParams['remember']);
  }

}
