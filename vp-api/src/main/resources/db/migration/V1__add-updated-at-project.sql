ALTER TABLE vp.public.projects
  ADD COLUMN updated_at timestamp default now() not null;