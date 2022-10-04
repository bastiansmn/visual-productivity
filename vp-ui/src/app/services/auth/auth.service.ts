import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin, UserRegister} from "../../model/user.model";
import {SocialUser} from "@abacritt/angularx-social-login";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {environment, environment as env} from "../../../environments/environment";
import {catchError, EMPTY, retry} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import {Error} from "../../model/error.model";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser?: User;
  isLoggedIn = false;

  readonly SAVE_FIELD = "loggedUser"

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  registerUser(userCredentials: UserRegister) {
    return new Promise((resolve, reject) => {
      const user = {
        email: userCredentials.email,
        name: userCredentials.firstname,
        lastname: userCredentials.lastname,
        password: userCredentials.password
      }

      this.loaderService.show();
      this.http.post<User>(`${env.apiBaseLink}/user/register`, user, { observe: 'response', withCredentials: true })
        .pipe(
          catchError((err: HttpErrorResponse) => {
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
            reject(err);

            return EMPTY
          })
        )
        .subscribe(
          (response: HttpResponse<User>) => {
            this.loaderService.hide();
            resolve(response);
        });
    })
  }

  loginWithVP(userCredentials: UserLogin) {
    return new Promise((resolve, reject) => {
      const formData = new FormData();
      formData.append('email', userCredentials.email);
      formData.append('password', userCredentials.password);

      this.loaderService.show();
      this.http.post<User>(`${env.apiBaseLink}/login`, formData, { observe: 'response', withCredentials: true })
        .pipe(
          retry({ count: 1, delay: 1_000 }),
          catchError(err => {
            this.alertService.show(
              err.error,
              { duration: 5000, type: AlertType.ERROR }
            )
            this.loaderService.hide();
            reject(err);

            return EMPTY
          })
        )
        .subscribe(
          (response: HttpResponse<User>) => {
            if (!response.body) return;
            // Saving user in session storage or local storage
            // TODO: Try to encrypt user infos for privacy reasons
            this.setConnection(response.body, userCredentials.remember);
            this.alertService.show(
              "Connecté",
              { duration: 3000, type: AlertType.SUCCESS }
            )
            this.loaderService.hide();
            resolve(response.body)
        });
    })
  }

  retrieveUserInfos() {
    const stringUser: string | null = sessionStorage.getItem(this.SAVE_FIELD) ?? localStorage.getItem(this.SAVE_FIELD);

    if (!stringUser) return;
    console.log("User connected, retrieving connection...");
    // Updating user infos
    const foundUser: User = JSON.parse(stringUser);
    this.loaderService.show();
    this.http.get<User>(`${environment.apiBaseLink}/user/fetchById?id=${foundUser.user_id}`, {observe: "response"})
      .pipe(
        catchError(err => {
          console.error(err);
          // TODO Refresh token
          this.loaderService.hide();
          this.alertService.show(
            "Vous avez été déconnecté",
            { duration: 3000, type: AlertType.ERROR }
          );
          this.logout();

          return EMPTY;
        })
      )
      .subscribe(response => {
        if (!response.body) return;
        if (response.ok) {
          this.setConnection(
            response.body,
            !!localStorage.getItem(this.SAVE_FIELD)
          );
          this.alertService.show("Connecté automatiquement", {
            duration: 5000,
            type: AlertType.INFO
          })
          setTimeout(() => {}, 5000);
          this.loaderService.hide();
        }
      })
  }

  setConnection(user: User, stayLogged: boolean) {
    this.loggedUser = user;
    this.isLoggedIn = true;
    this.clearConnection();
    if (stayLogged)
      localStorage.setItem("loggedUser", JSON.stringify(user));
    else
      sessionStorage.setItem("loggedUser", JSON.stringify(user));
    this.alertService.show(
      "Impossible de vous connecter, votre compte est peut-être désactivé ou bloqué",
      { duration: 5000, type: AlertType.ERROR }
    );
  }

  clearConnection() {
    sessionStorage.removeItem(this.SAVE_FIELD);
    localStorage.removeItem(this.SAVE_FIELD);
  }

  logout() {
    this.clearConnection();
    this.cookieService.deleteAll();
  }

  socialLogin(user: SocialUser, provider: LoginProvider) {
    console.log(user, provider);
  }
}
