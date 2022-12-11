import Label from "./label.model";
import Event from "./event.model";
import Goal from "./goal.model";
import {User} from "./user.model";

export default interface Project {
  project_id: number;
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
}
