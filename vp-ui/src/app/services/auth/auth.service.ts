import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin} from "../../model/user.model";
import {SocialUser} from "@abacritt/angularx-social-login";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment, environment as env} from "../../../environments/environment";
import {catchError, EMPTY, retry} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";

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

  loginWithVP(userCredentials: UserLogin) {
    return new Promise((resolve, reject) => {
      const formData = new FormData();
      formData.append('email', userCredentials.email);
      formData.append('password', userCredentials.password);

      this.loaderService.show();
      this.http.post<User>(`${env.apiBaseLink}/login`, formData, { observe: 'response', withCredentials: true })
        .pipe(
          retry({ count: 3, delay: 1_000 }),
          catchError(err => {
            console.log(err);
            this.loaderService.hide();
            this.alertService.show(
              "Impossible de vous connecter, votre compte est peut-être désactivé ou bloqué",
              { duration: 5000, type: AlertType.ERROR }
            )
            this.logout();
            reject(err);

            return EMPTY
          })
        )
        .subscribe(
          (response: HttpResponse<User>) => {
            if (!response.body) return;
            // Saving user in session storage or local storage
            // TODO: Try to encrypt user infos for privacy reasons
            this.setConnection(response.body);
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
          )
          this.logout();

          return EMPTY;
        })
      )
      .subscribe(response => {
        if (!response.body) return;
        if (response.ok) {
          this.setConnection(response.body);
          this.alertService.show("Connecté automatiquement", {
            duration: 5000,
            type: AlertType.INFO
          })
          setTimeout(() => {}, 5000);
          this.loaderService.hide();
        }
      })
  }

  setConnection(user: User) {
    this.loggedUser = user;
    this.isLoggedIn = true;
    if (sessionStorage.getItem(this.SAVE_FIELD))
      sessionStorage.setItem(this.SAVE_FIELD, JSON.stringify(user));
    else if (localStorage.getItem(this.SAVE_FIELD))
      localStorage.setItem(this.SAVE_FIELD, JSON.stringify(user));
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
