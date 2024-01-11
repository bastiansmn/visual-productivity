import {Injectable} from '@angular/core';
import {LoginProvider, User, UserLogin, UserRegister} from "../../model/user.model";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";
import {SocialUser} from "@abacritt/angularx-social-login";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  loggedUser = new BehaviorSubject<User | null>(null);
  isLoggedIn = new BehaviorSubject<boolean>(false);

  constructor(
    private http: HttpClient
  ) { }

  loginWithVP(user: UserLogin) {
    const formData = new FormData();
    formData.append("email", user.email);
    formData.append("password", user.password);

    return this.http.post<User>("/api/v1/login", formData)
  }

  register(user: UserRegister) {
    const { passwordConfirm, ...userWithoutPasswordConfirm } = user;
    const userWithProvider = {
      ...userWithoutPasswordConfirm,
      provider: LoginProvider.VP
    }

    return this.http.post<User>("/api/v1/user/register", userWithProvider)
  }

  confirmMail(confirmationCode: string) {
    const body = {
      confirmationCode: confirmationCode,
      user: this.loggedUser.value
    }

    return this.http.put("/api/v1/mail/confirm", body)
  }

  revalidateEmail() {
    return this.http.post("/api/v1/mail/revalidate", this.loggedUser.value)
  }

  refreshTokens() {
    return this.http.get<User>("/api/v1/token/refresh")
  }

  validateTokens() {
    return this.http.get<boolean>("/api/v1/token/validate")
  }

  getUserInfos() {
    return this.http.get<User>("/api/v1/user/me")
  }

  loginWithSocial(user: SocialUser) {
    return this.http.post<User>("/api/v1/oauth2/login", user)
  }


}
