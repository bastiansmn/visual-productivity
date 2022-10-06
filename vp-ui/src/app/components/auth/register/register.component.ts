import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  formGroup = this.fb.group({
    firstname: ['', Validators.required],
    lastname: ['', Validators.required],
    email: ['', [Validators.required, Validators.pattern("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")]],
    password: ['', [Validators.required, Validators.minLength(9)]],
    passwordConfirm: ['', [Validators.required, Validators.minLength(9)]],
    remember: [false]
  }, { validator: this.checkPasswords });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  register() {
    if (this.formGroup.valid) {
      this.authService.registerUser(this.formGroup.value).then(async () => {
        await this.router.navigate(['/confirm'], { queryParams: {remember: this.formGroup.value.remember} });
      }).catch(err => {
        console.error(err);
      })
    }
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
      case 'firstname':
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

  ngOnInit(): void {
  }

}
