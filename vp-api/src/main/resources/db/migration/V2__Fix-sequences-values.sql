--
-- PostgreSQL database dump
--

DROP TABLE IF EXISTS authorities CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;
DROP TABLE IF EXISTS goals CASCADE;
DROP TABLE IF EXISTS labels CASCADE;
DROP TABLE IF EXISTS link_goal_events CASCADE;
DROP TABLE IF EXISTS link_goal_labels CASCADE;
DROP TABLE IF EXISTS link_role_auths CASCADE;
DROP TABLE IF EXISTS link_user_projects CASCADE;
DROP TABLE IF EXISTS link_user_roles CASCADE;
DROP TABLE IF EXISTS mail_confirm CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS users CASCADE;

    --
-- Name: authorities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE authorities (
    auth_id bigint NOT NULL,
    name character varying(255) NOT NULL
);

--
-- Name: authorities_auth_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE authorities_auth_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: authorities_auth_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE authorities_auth_id_seq OWNED BY authorities.auth_id;


--
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE events (
    event_id bigint NOT NULL,
    date_end timestamp without time zone NOT NULL,
    date_start timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    whole_day boolean NOT NULL,
    project_id bigint NOT NULL
);

--
-- Name: events_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE events_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: events_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE events_event_id_seq OWNED BY events.event_id;


--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);

--
-- Name: goals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE goals (
    goal_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    date_start timestamp without time zone NOT NULL,
    deadline timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    project_id bigint
);

--
-- Name: goals_goal_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE goals_goal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: goals_goal_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE goals_goal_id_seq OWNED BY goals.goal_id;


--
-- Name: labels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE labels (
    label_id bigint NOT NULL,
    color character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    name character varying(255) NOT NULL,
    project_id bigint NOT NULL
);

--
-- Name: labels_label_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE labels_label_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: labels_label_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE labels_label_id_seq OWNED BY labels.label_id;


--
-- Name: link_goal_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE link_goal_events (
    goal_id bigint NOT NULL,
    event_id bigint NOT NULL
);

--
-- Name: link_goal_labels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE link_goal_labels (
    goal_id bigint NOT NULL,
    label_id bigint NOT NULL
);

--
-- Name: link_role_auths; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE link_role_auths (
    role_id bigint NOT NULL,
    auth_id bigint NOT NULL
);

--
-- Name: link_user_projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE link_user_projects (
    project_id bigint NOT NULL,
    user_id bigint NOT NULL
);

--
-- Name: link_user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE link_user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

--
-- Name: mail_confirm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE mail_confirm (
    confirmation_id bigint NOT NULL,
    attempts integer NOT NULL,
    confirmation_code character varying(255) NOT NULL,
    expiration_date timestamp without time zone NOT NULL,
    user_id bigint
);

--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE mail_confirm_confirmation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE mail_confirm_confirmation_id_seq OWNED BY mail_confirm.confirmation_id;


--
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE projects (
    project_id bigint NOT NULL,
    complete_mode boolean NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    deadline timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    token character varying(255) NOT NULL
);

--
-- Name: projects_project_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE projects_project_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: projects_project_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE projects_project_id_seq OWNED BY projects.project_id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE role (
    role_id bigint NOT NULL,
    name character varying(255) NOT NULL
);

--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE role_role_id_seq OWNED BY role.role_id;


--
-- Name: tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE tasks (
    task_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    date_end timestamp without time zone NOT NULL,
    date_start timestamp without time zone NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    goal_id bigint NOT NULL,
    project_id bigint NOT NULL
);

--
-- Name: tasks_task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE tasks_task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: tasks_task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE tasks_task_id_seq OWNED BY tasks.task_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    user_id bigint NOT NULL,
    created_date timestamp without time zone DEFAULT now() NOT NULL,
    email character varying(255) NOT NULL,
    is_enabled boolean NOT NULL,
    is_not_locked boolean NOT NULL,
    lastname character varying(255),
    name character varying(255) NOT NULL,
    password character varying(255),
    provider character varying(255) NOT NULL
);

--
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_user_id_seq OWNED BY users.user_id;


--
-- Name: authorities auth_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authorities ALTER COLUMN auth_id SET DEFAULT nextval('authorities_auth_id_seq'::regclass);


--
-- Name: events event_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY events ALTER COLUMN event_id SET DEFAULT nextval('events_event_id_seq'::regclass);


--
-- Name: goals goal_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY goals ALTER COLUMN goal_id SET DEFAULT nextval('goals_goal_id_seq'::regclass);


--
-- Name: labels label_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels ALTER COLUMN label_id SET DEFAULT nextval('labels_label_id_seq'::regclass);


--
-- Name: mail_confirm confirmation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mail_confirm ALTER COLUMN confirmation_id SET DEFAULT nextval('mail_confirm_confirmation_id_seq'::regclass);


--
-- Name: projects project_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY projects ALTER COLUMN project_id SET DEFAULT nextval('projects_project_id_seq'::regclass);


--
-- Name: role role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role ALTER COLUMN role_id SET DEFAULT nextval('role_role_id_seq'::regclass);


--
-- Name: tasks task_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tasks ALTER COLUMN task_id SET DEFAULT nextval('tasks_task_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN user_id SET DEFAULT nextval('users_user_id_seq'::regclass);


--
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO authorities (auth_id, name) VALUES (1, 'create');
INSERT INTO authorities (auth_id, name) VALUES (2, 'read');
INSERT INTO authorities (auth_id, name) VALUES (3, 'update');
INSERT INTO authorities (auth_id, name) VALUES (4, 'delete');


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) VALUES (1, '1', 'Initial', 'SQL', 'V1__Initial.sql', -2075999044, 'postgres', '2022-11-15 09:41:56.071093', 180, true);


--
-- Data for Name: goals; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: labels; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: link_goal_events; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: link_goal_labels; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: link_role_auths; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO link_role_auths (role_id, auth_id) VALUES (2, 1);
INSERT INTO link_role_auths (role_id, auth_id) VALUES (1, 4);
INSERT INTO link_role_auths (role_id, auth_id) VALUES (1, 1);
INSERT INTO link_role_auths (role_id, auth_id) VALUES (1, 3);
INSERT INTO link_role_auths (role_id, auth_id) VALUES (1, 2);


--
-- Data for Name: link_user_projects; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: link_user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO link_user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO link_user_roles (user_id, role_id) VALUES (1, 1);


--
-- Data for Name: mail_confirm; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO role (role_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (role_id, name) VALUES (2, 'ROLE_USER');


--
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO users (user_id, created_date, email, is_enabled, is_not_locked, lastname, name, password, provider) VALUES (1, '2022-11-14 17:40:29.044', 'bastian.somon@gmail.com', true, true, 'Somon', 'Bastian', '$2a$10$HUwF..ZtBFUnBbzQ7xua5ufPakbW2HAc4ixnX0/1QXXGUjua5dfh6', 'LOCAL');


--
-- Name: authorities_auth_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('authorities_auth_id_seq', 7, true);


--
-- Name: events_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('events_event_id_seq', 1, false);


--
-- Name: goals_goal_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('goals_goal_id_seq', 1, false);


--
-- Name: labels_label_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('labels_label_id_seq', 1, false);


--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('mail_confirm_confirmation_id_seq', 1, true);


--
-- Name: projects_project_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('projects_project_id_seq', 1, true);


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('role_role_id_seq', 2, true);


--
-- Name: tasks_task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('tasks_task_id_seq', 1, false);


--
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_user_id_seq', 1, true);


--
-- Name: authorities authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (auth_id);


--
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY events
    ADD CONSTRAINT events_pkey PRIMARY KEY (event_id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: goals goals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY goals
    ADD CONSTRAINT goals_pkey PRIMARY KEY (goal_id);


--
-- Name: labels labels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels
    ADD CONSTRAINT labels_pkey PRIMARY KEY (label_id);


--
-- Name: link_goal_events link_goal_events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_events
    ADD CONSTRAINT link_goal_events_pkey PRIMARY KEY (goal_id, event_id);


--
-- Name: link_goal_labels link_goal_labels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_labels
    ADD CONSTRAINT link_goal_labels_pkey PRIMARY KEY (goal_id, label_id);


--
-- Name: link_role_auths link_role_auths_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_role_auths
    ADD CONSTRAINT link_role_auths_pkey PRIMARY KEY (role_id, auth_id);


--
-- Name: link_user_projects link_user_projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_projects
    ADD CONSTRAINT link_user_projects_pkey PRIMARY KEY (project_id, user_id);


--
-- Name: link_user_roles link_user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_roles
    ADD CONSTRAINT link_user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: mail_confirm mail_confirm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mail_confirm
    ADD CONSTRAINT mail_confirm_pkey PRIMARY KEY (confirmation_id);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (project_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (task_id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: role uk_8sewwnpamngi6b1dwaa88askk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY role
    ADD CONSTRAINT uk_8sewwnpamngi6b1dwaa88askk UNIQUE (name);


--
-- Name: authorities uk_nb3atvjf9ov5d0egnuk47o5e; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authorities
    ADD CONSTRAINT uk_nb3atvjf9ov5d0egnuk47o5e UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON flyway_schema_history USING btree (success);


--
-- Name: link_goal_events fk1qiv347itkv6dicxwulry8qdh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_events
    ADD CONSTRAINT fk1qiv347itkv6dicxwulry8qdh FOREIGN KEY (event_id) REFERENCES events(event_id);


--
-- Name: link_user_projects fk3mnnd6yiw5ubon952bgifrgve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_projects
    ADD CONSTRAINT fk3mnnd6yiw5ubon952bgifrgve FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- Name: labels fk3sxl6x5sa83ojn87msxofr650; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels
    ADD CONSTRAINT fk3sxl6x5sa83ojn87msxofr650 FOREIGN KEY (project_id) REFERENCES projects(project_id);


--
-- Name: goals fk4nqfm6rifli0sxjcqo7t2npmw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY goals
    ADD CONSTRAINT fk4nqfm6rifli0sxjcqo7t2npmw FOREIGN KEY (project_id) REFERENCES projects(project_id);


--
-- Name: link_goal_labels fk59rwqu8edqkxplj4u2pjparnj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_labels
    ADD CONSTRAINT fk59rwqu8edqkxplj4u2pjparnj FOREIGN KEY (goal_id) REFERENCES goals(goal_id);


--
-- Name: link_role_auths fk9y54ispm3xsxqbcu8lsraipkv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_role_auths
    ADD CONSTRAINT fk9y54ispm3xsxqbcu8lsraipkv FOREIGN KEY (role_id) REFERENCES role(role_id);


--
-- Name: tasks fkacjm73w3vfy4x7gj42hd1ltkb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tasks
    ADD CONSTRAINT fkacjm73w3vfy4x7gj42hd1ltkb FOREIGN KEY (goal_id) REFERENCES goals(goal_id);


--
-- Name: events fkccfs1y85nru2df6x7pi8bk77n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY events
    ADD CONSTRAINT fkccfs1y85nru2df6x7pi8bk77n FOREIGN KEY (project_id) REFERENCES projects(project_id);


--
-- Name: link_user_projects fkeqd6bh0fl1b9a6qjq7fm40k42; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_projects
    ADD CONSTRAINT fkeqd6bh0fl1b9a6qjq7fm40k42 FOREIGN KEY (project_id) REFERENCES projects(project_id);


--
-- Name: link_user_roles fkftdowkufvuyw5fv3lkc1b4qnm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_roles
    ADD CONSTRAINT fkftdowkufvuyw5fv3lkc1b4qnm FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- Name: link_goal_labels fkmg6mjqplafwcfye10t90h7d4j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_labels
    ADD CONSTRAINT fkmg6mjqplafwcfye10t90h7d4j FOREIGN KEY (label_id) REFERENCES labels(label_id);


--
-- Name: mail_confirm fkpbqh0kc1y5mo5chwtwqhjnvur; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY mail_confirm
    ADD CONSTRAINT fkpbqh0kc1y5mo5chwtwqhjnvur FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- Name: link_user_roles fkq0v7iry67if2clupst37ewe8o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_user_roles
    ADD CONSTRAINT fkq0v7iry67if2clupst37ewe8o FOREIGN KEY (role_id) REFERENCES role(role_id);


--
-- Name: link_goal_events fkr5w6gwjf2qli1jw9r8mn6yw74; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_goal_events
    ADD CONSTRAINT fkr5w6gwjf2qli1jw9r8mn6yw74 FOREIGN KEY (goal_id) REFERENCES goals(goal_id);


--
-- Name: link_role_auths fkru8oe0bafjfp6gwm0b208r7bd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY link_role_auths
    ADD CONSTRAINT fkru8oe0bafjfp6gwm0b208r7bd FOREIGN KEY (auth_id) REFERENCES authorities(auth_id);


--
-- Name: tasks fksfhn82y57i3k9uxww1s007acc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY tasks
    ADD CONSTRAINT fksfhn82y57i3k9uxww1s007acc FOREIGN KEY (project_id) REFERENCES projects(project_id);


--
-- PostgreSQL database dump complete
--

