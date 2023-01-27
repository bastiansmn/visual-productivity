export interface LabelCreation {
  name: string;
  color: string;
  projectId: number;
}

export interface LabelAssignation {
  label_id: number;
  goal_id: number;
}

export default interface Label {
  label_id: number;
  name: string;
  color: string;
  created_at: Date;
}
