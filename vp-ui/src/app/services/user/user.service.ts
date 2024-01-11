import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User, UserModification} from "../../model/user.model";
import {EMPTY} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient
  ) { }

  updateUser(userUpdate: UserModification) {
    return this.http.put<User>('/api/v1/user/update', userUpdate)
  }

  updateAvatar(avatar: File) {
    if (!avatar) return EMPTY;
    const formData = new FormData();
    formData.append('avatar', avatar);
    return this.http.patch<User>('/api/v1/user/avatar', formData)
  }
}
