export default interface Task {
  task_id: number;
  name: string,
  description: string,
  date_start: Date,
  date_end: Date,
  created_at: Date,
  completed: boolean,
}
