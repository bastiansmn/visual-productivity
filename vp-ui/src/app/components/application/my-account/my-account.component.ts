import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../../services/auth/auth.service";
import {BehaviorSubject, take} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {UserService} from "../../../services/user/user.service";
import {User, UserModification} from "../../../model/user.model";

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.scss']
})
export class MyAccountComponent implements OnInit {

  private edition$ = new BehaviorSubject<boolean>(false);
  get isEditing() {
    return this.edition$.asObservable();
  }

  form!: FormGroup;

  get user() {
    return this.authService.loggedUser.asObservable();
  }

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private userService: UserService
  ) { }

  editUserInformations() {
    this.edition$.next(true);
  }

  ngOnInit(): void {
    this.authService.loggedUser.asObservable()
      .subscribe(user => {
        if (!user) return;
        this.form = this.fb.group({
          name: this.fb.control(user.name, [Validators.required]),
          lastname: this.fb.control(user.lastname),
          email: { value: user.email, disabled: true },
          avatar: this.fb.control<File | null>(null, [this.isJpegOrPng, this.notBiggerThan5MB, this.notBiggerThan1000x1000])
        })
      });
  }

  private isJpegOrPng(control: any) {
    if (control.value && control.value.type !== 'image/jpeg' && control.value.type !== 'image/png') {
      return {fileType: true};
    }
    return null;
  }

  private notBiggerThan5MB(control: any) {
    if (control.value && control.value.size > 5000000) {
      return {maxSize: true};
    }
    return null;
  }

  private notBiggerThan1000x1000(control: any) {
    if (control.value) {
      const img = new Image();
      img.src = URL.createObjectURL(control.value);
      img.onload = () => {
        if (img.width > 1000 || img.height > 1000) {
          return {maxSize: true};
        }
        return null;
      }
    }
    return null;
  }

  getErrorMessage(formControlName: string): string {
    switch (formControlName) {
      case 'name':
        if (this.form.controls[formControlName].hasError('required'))
          return "Précisez votre prénom";

        return "Une erreur inconnue est survenue";
      case 'lastname':
        if (this.form.controls[formControlName].hasError('required'))
          return "Précisez votre nom";

        return "Une erreur inconnue est survenue";
      case 'email':
        if (this.form.controls[formControlName].hasError('required'))
          return "Veuillez saisir un email";

        if (this.form.controls[formControlName].hasError('pattern'))
          return "Email invalide";

        return "Une erreur inconnue est survenue";

      case 'avatar':
        if (this.form.controls[formControlName].hasError('required'))
          return "Veuillez sélectionner un fichier";

        if (this.form.controls[formControlName].hasError('maxSize'))
          return "Le fichier est trop volumineux";

        if (this.form.controls[formControlName].hasError('fileType'))
          return "Le fichier n'est pas valide";

        return "Une erreur inconnue est survenue";
      default:
        return "Une erreur inconnue est survenue";
    }
  }

  updateUser() {
    if (this.form.invalid) return;

    this.user
      .pipe(take(1))
      .subscribe(user => {
        if (!user) return;
        const userUpdate = {
          id: user.user_id,
          email: user.email,
          name: this.form.controls['name'].value,
          lastname: this.form.controls['lastname'].value
        } as UserModification;
        this.userService.updateUser(userUpdate)
          .pipe(take(1))
          .subscribe((userReturned: User) => {
            this.edition$.next(false);
            this.authService.loggedUser.next(userReturned);
          });

        this.userService.updateAvatar(this.form.controls['avatar'].value)
          .pipe(take(1))
          .subscribe((userReturned: User) => {
            this.edition$.next(false);
            this.authService.loggedUser.next(userReturned);
          });
      });
  }

  handleFileUpload($event: File | undefined) {
    if (!$event) {
      this.form.controls['avatar'].setValue(null);
      return;
    }
    this.form.controls['avatar'].setValue($event);
  }
}
