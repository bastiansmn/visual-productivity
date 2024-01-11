import {Authority} from "./authority.model";

export interface Role {
  role_id: Number,
  name: string,
  authorities: Array<Authority>
}
