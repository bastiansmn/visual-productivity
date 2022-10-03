import {Role} from "./role.model";

export interface UserLogin {
  email: string,
  password: string,
  remember: boolean
}

export enum LoginProvider {
  VP = "VP",
  GOOGLE = "GOOGLE"
}

export interface User {
  user_id: Number,
  email: String,
  username: String,
  name: String,
  lastname: String,
  roles: Array<Role>,
  isEnabled: boolean,
  isNotLocked: boolean
}
