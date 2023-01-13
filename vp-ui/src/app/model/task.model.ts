export interface TaskCreation {
  name: string;
  description: string;
  date_start: Date;
  date_end: Date;
  goal_id: number;
  project_id: string;
}

export default interface Task {
  task_id: number;
  name: string,
  description: string,
  date_start: Date,
  date_end: Date,
  created_at: Date,
  completed: boolean,
}
