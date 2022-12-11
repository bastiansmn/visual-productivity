import Label from "./label.model";

export enum GoalStatus {
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
