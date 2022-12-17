--
-- PostgreSQL database dump
--


--
-- Name: authorities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.authorities (
    auth_id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: authorities_auth_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.authorities_auth_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: authorities_auth_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.authorities_auth_id_seq OWNED BY public.authorities.auth_id;


--
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.events (
    event_id bigint NOT NULL,
    date_end timestamp without time zone NOT NULL,
    date_start timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    whole_day boolean NOT NULL,
    project_id character varying(255) NOT NULL
);


--
-- Name: events_event_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.events_event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: events_event_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.events_event_id_seq OWNED BY public.events.event_id;



--
-- Name: goals; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.goals (
    goal_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    date_start timestamp without time zone NOT NULL,
    deadline timestamp without time zone NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    project_id character varying(255)
);



--
-- Name: goals_goal_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.goals_goal_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: goals_goal_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.goals_goal_id_seq OWNED BY public.goals.goal_id;


--
-- Name: labels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.labels (
    label_id bigint NOT NULL,
    color character varying(255) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    name character varying(255) NOT NULL,
    project_id character varying(255) NOT NULL
);



--
-- Name: labels_label_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.labels_label_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: labels_label_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.labels_label_id_seq OWNED BY public.labels.label_id;


--
-- Name: link_goal_events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.link_goal_events (
    goal_id bigint NOT NULL,
    event_id bigint NOT NULL
);



--
-- Name: link_goal_labels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.link_goal_labels (
    goal_id bigint NOT NULL,
    label_id bigint NOT NULL
);



--
-- Name: link_role_auths; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.link_role_auths (
    role_id bigint NOT NULL,
    auth_id bigint NOT NULL
);



--
-- Name: link_user_projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.link_user_projects (
    user_id bigint NOT NULL,
    project_id character varying(255) NOT NULL
);



--
-- Name: link_user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.link_user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);



--
-- Name: mail_confirm; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.mail_confirm (
    confirmation_id bigint NOT NULL,
    attempts integer NOT NULL,
    confirmation_code character varying(255) NOT NULL,
    expiration_date timestamp without time zone NOT NULL,
    user_id bigint NOT NULL
);



--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.mail_confirm_confirmation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.mail_confirm_confirmation_id_seq OWNED BY public.mail_confirm.confirmation_id;


--
-- Name: projects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.projects (
    project_id character varying(255) NOT NULL,
    complete_mode boolean NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    deadline timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    project_identifier character varying(255) NOT NULL,
    token character varying(255) NOT NULL
);



--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    role_id bigint NOT NULL,
    name character varying(255) NOT NULL
);



--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_role_id_seq OWNED BY public.role.role_id;


--
-- Name: tasks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tasks (
    task_id bigint NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    date_end timestamp without time zone NOT NULL,
    date_start timestamp without time zone NOT NULL,
    description character varying(255),
    name character varying(255) NOT NULL,
    goal_id bigint NOT NULL,
    project_id character varying(255) NOT NULL
);



--
-- Name: tasks_task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tasks_task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: tasks_task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tasks_task_id_seq OWNED BY public.tasks.task_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
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

CREATE SEQUENCE public.users_user_id_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- Name: authorities auth_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities ALTER COLUMN auth_id SET DEFAULT nextval('public.authorities_auth_id_seq'::regclass);


--
-- Name: events event_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events ALTER COLUMN event_id SET DEFAULT nextval('public.events_event_id_seq'::regclass);


--
-- Name: goals goal_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.goals ALTER COLUMN goal_id SET DEFAULT nextval('public.goals_goal_id_seq'::regclass);


--
-- Name: labels label_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.labels ALTER COLUMN label_id SET DEFAULT nextval('public.labels_label_id_seq'::regclass);


--
-- Name: mail_confirm confirmation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mail_confirm ALTER COLUMN confirmation_id SET DEFAULT nextval('public.mail_confirm_confirmation_id_seq'::regclass);


--
-- Name: role role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN role_id SET DEFAULT nextval('public.role_role_id_seq'::regclass);


--
-- Name: tasks task_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks ALTER COLUMN task_id SET DEFAULT nextval('public.tasks_task_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- Data for Name: authorities; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.authorities (auth_id, name) VALUES (1, 'create');
INSERT INTO public.authorities (auth_id, name) VALUES (2, 'read');
INSERT INTO public.authorities (auth_id, name) VALUES (3, 'update');
INSERT INTO public.authorities (auth_id, name) VALUES (4, 'delete');


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--


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

INSERT INTO public.link_role_auths (role_id, auth_id) VALUES (2, 1);
INSERT INTO public.link_role_auths (role_id, auth_id) VALUES (1, 1);
INSERT INTO public.link_role_auths (role_id, auth_id) VALUES (1, 2);
INSERT INTO public.link_role_auths (role_id, auth_id) VALUES (1, 4);
INSERT INTO public.link_role_auths (role_id, auth_id) VALUES (1, 3);


--
-- Data for Name: link_user_projects; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: link_user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.link_user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO public.link_user_roles (user_id, role_id) VALUES (1, 1);


--
-- Data for Name: mail_confirm; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: projects; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.role (role_id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.role (role_id, name) VALUES (2, 'ROLE_USER');


--
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (user_id, created_date, email, is_enabled, is_not_locked, lastname, name, password, provider) VALUES (1, '2022-12-17 17:16:08.625', 'bastian.somon@gmail.com', true, true, 'Somon', 'Bastian', '$2a$10$HNpBool/m9MsZNFrdMYAAeTB7VdlMJfvtvyHHeQmcEGnHhVRZKmVm', 'LOCAL');


--
-- Name: authorities_auth_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.authorities_auth_id_seq', 4, true);


--
-- Name: events_event_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.events_event_id_seq', 1, false);


--
-- Name: goals_goal_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.goals_goal_id_seq', 1, false);


--
-- Name: labels_label_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.labels_label_id_seq', 1, false);


--
-- Name: mail_confirm_confirmation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.mail_confirm_confirmation_id_seq', 1, true);


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_role_id_seq', 2, true);


--
-- Name: tasks_task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tasks_task_id_seq', 1, false);


--
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 1, true);


--
-- Name: authorities authorities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT authorities_pkey PRIMARY KEY (auth_id);


--
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (event_id);


--
-- Name: goals goals_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.goals
    ADD CONSTRAINT goals_pkey PRIMARY KEY (goal_id);


--
-- Name: labels labels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.labels
    ADD CONSTRAINT labels_pkey PRIMARY KEY (label_id);


--
-- Name: link_goal_events link_goal_events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_events
    ADD CONSTRAINT link_goal_events_pkey PRIMARY KEY (goal_id, event_id);


--
-- Name: link_goal_labels link_goal_labels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_labels
    ADD CONSTRAINT link_goal_labels_pkey PRIMARY KEY (goal_id, label_id);


--
-- Name: link_role_auths link_role_auths_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_role_auths
    ADD CONSTRAINT link_role_auths_pkey PRIMARY KEY (role_id, auth_id);


--
-- Name: link_user_projects link_user_projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_projects
    ADD CONSTRAINT link_user_projects_pkey PRIMARY KEY (project_id, user_id);


--
-- Name: link_user_roles link_user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_roles
    ADD CONSTRAINT link_user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: mail_confirm mail_confirm_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mail_confirm
    ADD CONSTRAINT mail_confirm_pkey PRIMARY KEY (confirmation_id);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (project_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (task_id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: role uk_8sewwnpamngi6b1dwaa88askk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT uk_8sewwnpamngi6b1dwaa88askk UNIQUE (name);


--
-- Name: projects uk_d2odc28mq6cesjqfpe1mq7srs; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT uk_d2odc28mq6cesjqfpe1mq7srs UNIQUE (project_identifier);


--
-- Name: authorities uk_nb3atvjf9ov5d0egnuk47o5e; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT uk_nb3atvjf9ov5d0egnuk47o5e UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- Name: link_goal_events fk1qiv347itkv6dicxwulry8qdh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_events
    ADD CONSTRAINT fk1qiv347itkv6dicxwulry8qdh FOREIGN KEY (event_id) REFERENCES public.events(event_id);


--
-- Name: link_user_projects fk3mnnd6yiw5ubon952bgifrgve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_projects
    ADD CONSTRAINT fk3mnnd6yiw5ubon952bgifrgve FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: labels fk3sxl6x5sa83ojn87msxofr650; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.labels
    ADD CONSTRAINT fk3sxl6x5sa83ojn87msxofr650 FOREIGN KEY (project_id) REFERENCES public.projects(project_id);


--
-- Name: goals fk4nqfm6rifli0sxjcqo7t2npmw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.goals
    ADD CONSTRAINT fk4nqfm6rifli0sxjcqo7t2npmw FOREIGN KEY (project_id) REFERENCES public.projects(project_id);


--
-- Name: link_goal_labels fk59rwqu8edqkxplj4u2pjparnj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_labels
    ADD CONSTRAINT fk59rwqu8edqkxplj4u2pjparnj FOREIGN KEY (goal_id) REFERENCES public.goals(goal_id);


--
-- Name: link_role_auths fk9y54ispm3xsxqbcu8lsraipkv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_role_auths
    ADD CONSTRAINT fk9y54ispm3xsxqbcu8lsraipkv FOREIGN KEY (role_id) REFERENCES public.role(role_id);


--
-- Name: tasks fkacjm73w3vfy4x7gj42hd1ltkb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fkacjm73w3vfy4x7gj42hd1ltkb FOREIGN KEY (goal_id) REFERENCES public.goals(goal_id);


--
-- Name: events fkccfs1y85nru2df6x7pi8bk77n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT fkccfs1y85nru2df6x7pi8bk77n FOREIGN KEY (project_id) REFERENCES public.projects(project_id);


--
-- Name: link_user_projects fkeqd6bh0fl1b9a6qjq7fm40k42; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_projects
    ADD CONSTRAINT fkeqd6bh0fl1b9a6qjq7fm40k42 FOREIGN KEY (project_id) REFERENCES public.projects(project_id);


--
-- Name: link_user_roles fkftdowkufvuyw5fv3lkc1b4qnm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_roles
    ADD CONSTRAINT fkftdowkufvuyw5fv3lkc1b4qnm FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: link_goal_labels fkmg6mjqplafwcfye10t90h7d4j; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_labels
    ADD CONSTRAINT fkmg6mjqplafwcfye10t90h7d4j FOREIGN KEY (label_id) REFERENCES public.labels(label_id);


--
-- Name: mail_confirm fkpbqh0kc1y5mo5chwtwqhjnvur; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.mail_confirm
    ADD CONSTRAINT fkpbqh0kc1y5mo5chwtwqhjnvur FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- Name: link_user_roles fkq0v7iry67if2clupst37ewe8o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_user_roles
    ADD CONSTRAINT fkq0v7iry67if2clupst37ewe8o FOREIGN KEY (role_id) REFERENCES public.role(role_id);


--
-- Name: link_goal_events fkr5w6gwjf2qli1jw9r8mn6yw74; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_goal_events
    ADD CONSTRAINT fkr5w6gwjf2qli1jw9r8mn6yw74 FOREIGN KEY (goal_id) REFERENCES public.goals(goal_id);


--
-- Name: link_role_auths fkru8oe0bafjfp6gwm0b208r7bd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.link_role_auths
    ADD CONSTRAINT fkru8oe0bafjfp6gwm0b208r7bd FOREIGN KEY (auth_id) REFERENCES public.authorities(auth_id);


--
-- Name: tasks fksfhn82y57i3k9uxww1s007acc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fksfhn82y57i3k9uxww1s007acc FOREIGN KEY (project_id) REFERENCES public.projects(project_id);


--
-- PostgreSQL database dump complete
--

