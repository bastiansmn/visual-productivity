ALTER TABLE projects
    DROP CONSTRAINT projects_pkey CASCADE;

ALTER TABLE projects
    ADD COLUMN temp_project_id varchar;
UPDATE projects
SET temp_project_id = project_id;
ALTER TABLE projects
    ALTER COLUMN project_id TYPE varchar(255);

ALTER TABLE projects
    ADD PRIMARY KEY (project_id);

ALTER TABLE link_user_projects
    ADD COLUMN temp varchar;
UPDATE link_user_projects
SET temp = project_id;
ALTER TABLE link_user_projects
    ALTER COLUMN project_id TYPE UUID USING null;
UPDATE link_user_projects
SET project_id = (SELECT temp_project_id FROM projects WHERE temp = temp_project_id);
ALTER TABLE link_user_projects
    ADD CONSTRAINT link_user_projects_project_fkey FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE labels
    ADD COLUMN temp varchar;
UPDATE labels
SET temp = project_id;
ALTER TABLE labels
    ALTER COLUMN project_id TYPE UUID USING null;
UPDATE labels
SET project_id = (SELECT temp_project_id FROM projects WHERE temp = temp_project_id);
ALTER TABLE labels
    ADD CONSTRAINT labels_project_fkey FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE tasks
    ADD COLUMN temp varchar;
UPDATE tasks
SET temp = project_id;
ALTER TABLE tasks
    ALTER COLUMN project_id TYPE UUID USING null;
UPDATE tasks
SET project_id = (SELECT temp_project_id FROM projects WHERE temp = temp_project_id);
ALTER TABLE tasks
    ADD CONSTRAINT tasks_project_fkey FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE goals
    ADD COLUMN temp varchar;
UPDATE goals
SET temp = project_id;
ALTER TABLE goals
    ALTER COLUMN project_id TYPE UUID USING null;
UPDATE goals
SET project_id = (SELECT temp_project_id FROM projects WHERE temp = temp_project_id);
ALTER TABLE goals
    ADD CONSTRAINT goals_project_fkey FOREIGN KEY (project_id) REFERENCES projects(project_id);

ALTER TABLE pending_user_invites
    RENAME COLUMN project TO project_id;
ALTER TABLE pending_user_invites
    ADD COLUMN temp varchar;
UPDATE pending_user_invites
SET temp = project_id;
ALTER TABLE pending_user_invites
    ALTER COLUMN project_id TYPE UUID USING null;
UPDATE pending_user_invites
SET project_id = (SELECT temp_project_id FROM projects WHERE temp = temp_project_id);
ALTER TABLE pending_user_invites
    ADD CONSTRAINT pending_user_invites_project_fkey FOREIGN KEY (project_id) REFERENCES projects(project_id);

