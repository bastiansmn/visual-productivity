import {Role} from "./role.model";

export interface UserLogin {
  email: string,
  password: string,
  remember: boolean
}

export interface UserRegister {
  email: string,
  password: string,
  passwordConfirm: string,
  name: string,
  lastname: string
}

export enum LoginProvider {
  VP = "LOCAL",
  GOOGLE = "GOOGLE"
}

export interface User {
  user_id: Number,
  email: String,
  username: String,
  name: String,
  lastname: String,
  roles: Array<Role>,
  enabled: boolean,
  notLocked: boolean
}
