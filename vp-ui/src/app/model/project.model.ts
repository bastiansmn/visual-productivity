import Label from "./label.model";
import Event from "./event.model";
import Goal from "./goal.model";
import {User} from "./user.model";

export interface ProjectCreation {
  name: string;
  description: string;
  deadline: Date;
  complete_mode: boolean;
}

export default interface Project {
  projectId: string;
  name: string;
  description: string;
  token: string;
  deadline: Date,
  complete_mode: boolean;
  allLabels: Label[];
  allEvents: Event[];
  allGoals: Goal[];
  allTasks: Task[];
  users: User[];
  created_at: Date;
  updated_at: Date;
}
