import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User, UserModification} from "../../model/user.model";
import {catchError, EMPTY} from "rxjs";
import {handleError} from "../../utils/http-error-handler.util";
import {LoaderService} from "../loader/loader.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private loaderService: LoaderService,
    private alertService: AlertService
  ) { }

  updateUser(userUpdate: UserModification) {
    return this.http.put<User>('/api/v1/user/update', userUpdate)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      );
  }

  updateAvatar(avatar: File) {
    if (!avatar) return EMPTY;
    const formData = new FormData();
    formData.append('avatar', avatar);
    return this.http.patch<User>('/api/v1/user/avatar', formData)
      .pipe(
        catchError(err => handleError(err, {
          loaderService: this.loaderService,
          alertService: this.alertService
        }))
      );
  }
}
