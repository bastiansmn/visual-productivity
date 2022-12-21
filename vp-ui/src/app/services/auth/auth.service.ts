import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin, UserRegister} from "../../model/user.model";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {BehaviorSubject, catchError} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {AlertService} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import {SocialUser} from "@abacritt/angularx-social-login";
import {handleError} from "../../utils/http-error-handler.util";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser = new BehaviorSubject<User | null>(null);
  isLoggedIn = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private alertService: AlertService,
    private loaderService: LoaderService,
    private router: Router
  ) { }

  loginWithVP(user: UserLogin) {
    const formData = new FormData();
    formData.append("email", user.email);
    formData.append("password", user.password);

    return this.http.post<User>("/api/v1/login", formData)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService,
          cookieService: this.cookieService
        }))
      )
  }

  register(user: UserRegister) {
    const { passwordConfirm, ...userWithoutPasswordConfirm } = user;
    const userWithProvider = {
      ...userWithoutPasswordConfirm,
      provider: LoginProvider.VP
    }

    return this.http.post<User>("/api/v1/user/register", userWithProvider)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService,
          cookieService: this.cookieService
        }))
      )
  }

  confirmMail(confirmationCode: string) {
    const body = {
      confirmationCode: confirmationCode,
      user: this.loggedUser.value
    }

    return this.http.put("/api/v1/mail/confirm", body)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  revalidateEmail() {
    return this.http.post("/api/v1/mail/revalidate", this.loggedUser.value)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  refreshTokens() {
    return this.http.get<User>("/api/v1/token/refresh")
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService,
          cookieService: this.cookieService
        }))
      )
  }

  validateTokens() {
    return this.http.get<boolean>("/api/v1/token/validate")
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService,
          cookieService: this.cookieService,
          router: this.router
        }))
      )
  }

  getUserInfos() {
    return this.http.get<User>("/api/v1/user/me")
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }

  loginWithSocial(user: SocialUser) {
    return this.http.post<User>("/api/v1/oauth2/login", user)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      )
  }


}
