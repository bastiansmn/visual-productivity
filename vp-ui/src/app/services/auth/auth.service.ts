import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin} from "../../model/user.model";
import {SocialUser} from "@abacritt/angularx-social-login";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {environment, environment as env} from "../../../environments/environment";
import {catchError, EMPTY, retry} from "rxjs";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser?: User;
  isLoggedIn = false;

  readonly SAVE_FIELD = "loggedUser"

  constructor(
    private http: HttpClient,
    private cookieService: CookieService
  ) { }

  loginWithVP(userCredentials: UserLogin) {
    return new Promise((resolve, reject) => {
      const formData = new FormData();
      formData.append('email', userCredentials.email);
      formData.append('password', userCredentials.password);

      this.http.post<User>(`${env.apiBaseLink}/login`, formData, { observe: 'response', withCredentials: true })
        .pipe(
          retry({ count: 3, delay: 1_000 }),
          catchError(err => {
            // TODO: Add loading and alert
            reject(err)
            return EMPTY
          })
        )
        .subscribe(
          (response: HttpResponse<User>) => {
            if (!response.body) return;
            this.loggedUser = response.body;
            this.isLoggedIn = true;
            this.clearConnection();
            if (userCredentials.remember)
              localStorage.setItem(this.SAVE_FIELD, JSON.stringify(response.body));
            else
              sessionStorage.setItem(this.SAVE_FIELD, JSON.stringify(response.body));
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
    this.http.get<User>(`${environment.apiBaseLink}/user/fetchById?id=${foundUser.user_id}`, {observe: "response"})
      .subscribe(response => {
        // TODO Handle disabled/locked account + alert
        if (!response.body) return;
        if (response.ok) {
          if (sessionStorage.getItem(this.SAVE_FIELD))
            sessionStorage.setItem(this.SAVE_FIELD, JSON.stringify(response.body));
          else if (localStorage.getItem(this.SAVE_FIELD))
            localStorage.setItem(this.SAVE_FIELD, JSON.stringify(response.body));
          this.isLoggedIn = true;
          this.loggedUser = response.body;
        }
      })
  }

  logout() {
    this.clearConnection();
    this.cookieService.deleteAll();
  }

  clearConnection() {
    sessionStorage.removeItem(this.SAVE_FIELD);
    localStorage.removeItem(this.SAVE_FIELD);
  }

  socialLogin(user: SocialUser, provider: LoginProvider) {
    console.log(user, provider);
  }
}
