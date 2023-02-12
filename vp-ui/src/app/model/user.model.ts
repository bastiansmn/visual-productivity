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

export interface UserModification {
  id: number,
  name: string,
  lastname: string,
  email: string,
}

export enum LoginProvider {
  VP = "LOCAL",
  GOOGLE = "GOOGLE"
}

export interface User {
  user_id: Number,
  email: string,
  username: string,
  avatar: string,
  name: string,
  lastname: string,
  roles: Array<Role>,
  enabled: boolean,
  notLocked: boolean
}
