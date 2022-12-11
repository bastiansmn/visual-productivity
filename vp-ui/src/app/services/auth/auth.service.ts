import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin, UserRegister} from "../../model/user.model";
import {SocialUser} from "@abacritt/angularx-social-login";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {BehaviorSubject, catchError, EMPTY, retry, Subject} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {AlertService, AlertType} from "../alert/alert.service";
import {LoaderService} from "../loader/loader.service";
import {Error} from "../../model/error.model";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser = new BehaviorSubject<User | null>(null);
  isLoggedIn = new BehaviorSubject<boolean>(false);

  readonly SAVE_FIELD = "loggedUser"

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private alertService: AlertService,
    private loaderService: LoaderService
  ) { }

  checkConnection() {
    const userStr: string | null = sessionStorage.getItem(this.SAVE_FIELD) ?? localStorage.getItem(this.SAVE_FIELD);
    const user: User = userStr ? JSON.parse(userStr) : null;
    this.isLoggedIn.next(!!user);
  }

  initAuthFlow() {
    return new Promise<boolean>((resolve, reject) => {
      const userStr: string | null = sessionStorage.getItem(this.SAVE_FIELD) ?? localStorage.getItem(this.SAVE_FIELD);
      const user: User = userStr ? JSON.parse(userStr) : null;
      if (!user) {
        this.isLoggedIn.next(false);
        this.clearConnection();
        reject();
        return;
      }

      this.checkTokensValidity()
        .then(async isValid => {
          if (isValid) {
            this.retrieveUserInfos(user)
              .catch(err => {
                console.error(err);
                this.isLoggedIn.next(false);
                reject();
              });
            resolve(true);
            return;
          }

          // Refresh token automatically if user wants to stay connected
          if (this.stayedLogged())
            await this.refreshTokens();
          else {
            this.alertService.show(
              "Vous avez été déconnecté",
              { duration: 5000, type: AlertType.WARNING }
            );
            this.logout();
            reject();
          }

        })
        .catch(() => {
          this.alertService.show(
            "Validation de votre identifiant impossible, reconnectez-vous",
            { duration: 5000, type: AlertType.ERROR }
          );
          this.logout();
          reject();
        })
    })
  }

  validateCode(code: String) {
    return new Promise((resolve, reject) => {
      this.loaderService.show();
      this.http.put("/api/v1/mail/confirm", {
        user: {
          user_id: this.loggedUser.getValue()?.user_id
        },
        confirmationCode: code
      }, { observe: 'response' })
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
          (res: HttpResponse<any>) => {
            this.loaderService.hide();
            this.alertService.show(
              "Votre compte a été validé",
              { duration: 5000, type: AlertType.SUCCESS }
            );
            resolve(res);
        });
    });
  }

  resendValidationCode() {
    const user = this.loggedUser;
    return new Promise((resolve, reject) => {
      this.loaderService.show();
      this.http.post("/api/v1/mail/revalidate", user, { observe: 'response' })
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
        .subscribe((res: HttpResponse<any>) => {
          this.alertService.show(
            "Vous allez recevoir un mail de confirmation",
            { type: AlertType.INFO, duration: 5000 }
          );
          this.loaderService.hide();
          resolve(res);
        });
    });
  }

  registerUser(userCredentials: UserRegister) {
    return new Promise((resolve, reject) => {
      const user = {
        email: userCredentials.email,
        name: userCredentials.firstname,
        lastname: userCredentials.lastname,
        password: userCredentials.password,
        provider: 'LOCAL'
      }

      this.loaderService.show();
      this.http.post<User>("/api/v1/user/register", user, { observe: 'response' })
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
            if (!response.body) return;
            this.alertService.show(
              "Vous allez recevoir un mail de confirmation",
              { type: AlertType.INFO, persistent: true }
            )
            this.loggedUser.next(response.body);
            this.isLoggedIn.next(false);
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
      this.http.post<User>("/api/v1/login", formData, { observe: 'response' })
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

  private retrieveUserInfos(foundUser: User) {
    return new Promise((resolve, reject) => {
      // Updating user infos
      this.loaderService.show();
      this.http.get<User>(`/api/v1/user/fetchByEmail?email=${foundUser.email}`, {observe: "response"})
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
            reject("Impossible de récupérer les informations utilisateur");

            return EMPTY;
          })
        )
        .subscribe(response => {
          if (!response.body) return;
          if (response.ok) {
            this.setConnection(
              response.body,
              this.stayedLogged()
            );
            this.alertService.show("Reconnecté automatiquement", {
              duration: 5000,
              type: AlertType.INFO
            })
            setTimeout(() => {}, 5000);
            this.loaderService.hide();
            resolve(null);
          }
        })
    })
  }

  private setConnection(user: User, stayLogged: boolean) {
    this.loggedUser.next(user);
    this.isLoggedIn.next(true);
    this.clearConnection();
    if (stayLogged)
      localStorage.setItem("loggedUser", JSON.stringify(user));
    else
      sessionStorage.setItem("loggedUser", JSON.stringify(user));
  }

  private clearConnection() {
    sessionStorage.removeItem(this.SAVE_FIELD);
    localStorage.removeItem(this.SAVE_FIELD);
  }

  logout() {
    this.clearConnection();
    this.cookieService.deleteAll();
  }

  socialLogin(user: SocialUser, provider: LoginProvider) {
    switch (provider) {
      case LoginProvider.GOOGLE:
        return this.googleLogin(user);
      default:
        return Promise.reject("Provider not supported");
    }
  }

  private googleLogin(user: SocialUser) {
    return new Promise((resolve, reject) => {
      this.loaderService.show();
      this.http.post<User>("/api/v1/oauth2/login", user, { observe: 'response' })
        .pipe(
          catchError(err => {
            this.alertService.show(
              err.error,
              { duration: 5000, type: AlertType.ERROR }
            )
            this.loaderService.hide();
            reject();
            return EMPTY
          })
        )
        .subscribe(response => {
          if (!response.body) return;
          this.loaderService.hide();
          this.alertService.show(
            "Connecté",
            { duration: 3000, type: AlertType.SUCCESS }
          );
          this.setConnection(response.body, true);
          resolve(response.body);
        })
    });
  }

  private checkTokensValidity() {
    return new Promise<Boolean>((resolve, reject) => {
      this.loaderService.show();
      this.http.get<Boolean>("/api/v1/token/validate", { observe: 'response' })
        .pipe(
          catchError(err => {
            this.alertService.show(
              err.error,
              { duration: 5000, type: AlertType.ERROR }
            )
            this.loaderService.hide();
            reject();
            return EMPTY
          })
        )
        .subscribe(response => {
          if (response.body === null) {
            this.loaderService.hide();
            reject();
            return;
          }
          resolve(response.body);
          this.loaderService.hide();
        })
    });
  }

  private refreshTokens() {
    return new Promise((resolve, reject) => {
      this.loaderService.show();
      this.http.get<User>("/api/v1/token/refresh", { observe: 'response' })
        .pipe(
          catchError(err => {
            this.alertService.show(
              err.error,
              { duration: 5000, type: AlertType.ERROR }
            )
            this.loaderService.hide();
            reject();
            return EMPTY
          })
        )
        .subscribe(response => {
          if (!response.body) return;
          this.loaderService.hide();
          this.alertService.show(
            "Connexion mise à jour",
            { duration: 3000, type: AlertType.SUCCESS }
          );
          this.setConnection(response.body, this.stayedLogged());
          resolve(response.body);
        })
    });
  }

  private stayedLogged() {
    return !!localStorage.getItem(this.SAVE_FIELD);
  }
}
