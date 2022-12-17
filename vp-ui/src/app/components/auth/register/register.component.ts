import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth/auth.service";
import {Router} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {LoaderService} from "../../../services/loader/loader.service";
import {AlertService} from "../../../services/alert/alert.service";
import {UserRegister} from "../../../model/user.model";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy, AfterViewInit {

  formGroup = this.fb.group({
    name: ['', Validators.required],
    lastname: ['', Validators.required],
    email: ['', [Validators.required, Validators.pattern("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")]],
    password: ['', [Validators.required, Validators.minLength(9)]],
    passwordConfirm: ['', [Validators.required, Validators.minLength(9)]],
    remember: [false]
  }, { validator: this.checkPasswords });
  componentDestroyed$: Subject<boolean> = new Subject();

  constructor(
    private authService: AuthService,
    private loaderService: LoaderService,
    private alertService: AlertService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  register() {
    if (this.formGroup.invalid) return;

    this.loaderService.show();
    this.authService.register(this.formGroup.value as UserRegister)
      .pipe(
        takeUntil(this.componentDestroyed$)
      )
      .subscribe(async user => {
        this.loaderService.hide();
        this.authService.loggedUser.next(user);
        await this.router.navigate(['/confirm'], { queryParams: { remember: this.formGroup.value.remember } });
      })
  }

  checkPasswords(formGroup: FormGroup) {
    const password = formGroup.controls['password'].value;
    const passwordConfirm = formGroup.controls['passwordConfirm'].value;
    // If one of the field isn't set ignore
    if (!password || !passwordConfirm) return null;
    // If both are same remove errors and no errors returned
    if (password === passwordConfirm) {
      formGroup.controls['password'].setErrors(null);
      formGroup.controls['passwordConfirm'].setErrors(null);
      return null;
    }
    // Set errors
    formGroup.controls['password'].setErrors({ passwordDontMatch: true });
    formGroup.controls['passwordConfirm'].setErrors({ passwordDontMatch: true });
    return { passwordDontMatch: true }
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'name':
        if (this.formGroup.controls[formControlName].hasError('required'))
          return "Précisez votre prénom";

        return "Une erreur inconnue est survenue";
      case 'lastname':
        if (this.formGroup.controls[formControlName].hasError('required'))
          return "Précisez votre nom";

        return "Une erreur inconnue est survenue";
      case 'email':
        if (this.formGroup.controls[formControlName].hasError('required'))
          return "Veuillez saisir un email";

        if (this.formGroup.controls[formControlName].hasError('pattern'))
          return "Email invalide";

        return "Une erreur inconnue est survenue";
      case 'password':
        if (this.formGroup.controls[formControlName].hasError('required'))
          return "Veuillez saisir un mot de passe";

        if (this.formGroup.controls[formControlName].hasError('minlength'))
          return "Votre mot de passe est trop court"

        if (this.formGroup.controls[formControlName].hasError('passwordDontMatch'))
          return "Les mots de passe ne correspondent pas"

        return "Une erreur inconnue est survenue";
      case 'passwordConfirm':
        if (this.formGroup.controls[formControlName].hasError('required'))
          return "Veuillez confirmez votre mot de passe";

        if (this.formGroup.controls[formControlName].hasError('minlength'))
          return "Votre mot de passe est trop court"

        if (this.formGroup.controls[formControlName].hasError('passwordDontMatch'))
          return "Les mots de passe ne correspondent pas"

        return "Une erreur inconnue est survenue";
      default:
        return "Une erreur inconnue est survenue";
    }
  }

  ngAfterViewInit(): void {
    (document.querySelector('input#name') as HTMLInputElement)?.focus();
  }

  ngOnDestroy(): void {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

}
