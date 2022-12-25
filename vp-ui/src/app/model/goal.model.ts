import Label from "./label.model";
import Task from "./task.model";

export enum GoalStatus {
  TODO = "TODO",
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE',
}

export default interface Goal {
  goal_id: number;
  name: string;
  description: string;
  date_start: Date;
  deadline: Date;
  status: GoalStatus,
  labels: Label[],
  tasks: Task[],
  created_at: Date;
}

export interface GoalCreation {
  date_start: Date;
  deadline: Date;
  description: string
  goalStatus: GoalStatus
  name: string
  project_id: string
}
