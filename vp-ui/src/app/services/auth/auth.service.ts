import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin, UserRegister} from "../../model/user.model";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {BehaviorSubject, catchError, EMPTY} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import {Error} from "../../model/error.model";
import {SocialUser} from "@abacritt/angularx-social-login";

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
    private loaderService: LoaderService
  ) { }

  loginWithVP(user: UserLogin) {
    const formData = new FormData();
    formData.append("email", user.email);
    formData.append("password", user.password);

    return this.http.post<User>("/api/v1/login", formData)
      .pipe(
        catchError(this.handleError)
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
        catchError(this.handleError)
      )
  }

  confirmMail(confirmationCode: string) {
    const body = {
      confirmationCode: confirmationCode,
      user: {
        user_id: this.loggedUser.value?.user_id
      }
    }

    return this.http.put("/api/v1/mail/confirm", body)
      .pipe(
        catchError(this.handleError)
      )
  }

  revalidateEmail() {
    return this.http.post("/api/v1/mail/revalidate", this.loggedUser.value)
      .pipe(
        catchError(this.handleError)
      )
  }

  refreshTokens() {
    return this.http.get<User>("/api/v1/token/refresh")
      .pipe(
        catchError(this.handleError)
      )
  }

  validateTokens() {
    return this.http.get<boolean>("/api/v1/token/validate")
      .pipe(
        catchError(this.handleError)
      )
  }

  getUserInfos() {
    return this.http.get<User>("/api/v1/user/me")
      .pipe(
        catchError(this.handleError)
      )
  }

  loginWithSocial(user: SocialUser) {
    return this.http.post<User>("/api/v1/oauth2/login", user)
      .pipe(
        catchError(this.handleError)
      )
  }

  handleError(err: HttpErrorResponse) {
    console.log(err);
    const error = err.error as Error;
    if (!environment.production) {
      console.error(error.devMessage);
      console.error(error.httpStatus, error.httpStatusString);
    }
    this.alertService.show(
      error.message,
      { duration: 5000, type: AlertType.ERROR }
    )
    this.loaderService.hide();
    return EMPTY;
  }

}
