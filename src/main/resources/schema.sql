drop table if exists tasks cascade;
drop table if exists stories cascade;
drop table if exists epics cascade;
drop table if exists bugs cascade;
drop table if exists project_users;
drop table if exists projects;
drop table if exists sprint;
drop table if exists backlog;
drop table if exists user_roles;
drop table if exists users;

drop sequence if exists global_seq;

create sequence global_seq start with 100000;

create table users
(
    id       integer default nextval('global_seq'),
    email    varchar(255) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    constraint users_pkey primary key (id),
    constraint uk_email unique (email)
);

create table user_roles
(
    user_id integer not null,
    roles   varchar(255),
    constraint fk_user_roles_user_id_users foreign key (user_id) references users (id)
        on delete cascade
);

create table backlog
(
    id integer default nextval('global_seq'),
    constraint backlog_pkey primary key (id)
);

create table sprint
(
    id integer default nextval('global_seq'),
    constraint sprint_pkey primary key (id)
);

create table projects
(
    id            integer default nextval('global_seq'),
    administrator varchar(255) not null,
    department    varchar(255) not null,
    description   varchar(255) not null,
    manager       varchar(255) not null,
    name          varchar(255) not null,
    backlog_id    integer,
    sprint_id     integer,
    constraint projects_pkey primary key (id),
    constraint fk_projects_backlog_id_backlog foreign key (backlog_id) references backlog (id),
    constraint fk_projects_sprint_id_sprint foreign key (sprint_id) references sprint (id)
);

create table project_users
(
    project_id integer not null,
    user_id    integer not null,
    constraint project_users_pkey primary key (project_id, user_id),
    constraint fk_project_users_project_id_project foreign key (user_id) references users (id)
        on delete cascade,
    constraint fk_project_users_user_id_users foreign key (project_id) references projects (id)
        on delete cascade
);

create table bugs
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      integer not null,
    status        varchar(255) not null,
    executor_id   integer,
    reporter_id   integer,
    backlog_id    integer,
    root_epic_id  integer,
    root_story_id integer,
    root_task_id  integer,
    sprint_id     integer,
    constraint bugs_pkey primary key (id),
    constraint fk_bugs_executor_id_users foreign key (executor_id) references users (id),
    constraint fk_bugs_reporter_id_users foreign key (reporter_id) references users (id),
    constraint fk_bugs_backlog_id_backlog foreign key (backlog_id) references backlog (id)
        on delete cascade,
    constraint fk_bugs_sprint_id_sprint foreign key (sprint_id) references sprint (id)
        on delete cascade
);

create index idx_bugs_creation_date on bugs (creation_date);
create index idx_bugs_name on bugs (name);
create index idx_bugs_priority on bugs (priority);
create index idx_bugs_status on bugs (status);
create index idx_bugs_executor_id on bugs (executor_id);
create index idx_bugs_reporter_id on bugs (reporter_id);

create table epics
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      integer not null,
    status        varchar(255) not null,
    executor_id   integer,
    reporter_id   integer,
    backlog_id    integer,
    root_bug_id   integer,
    root_story_id integer,
    root_task_id  integer,
    sprint_id     integer,
    constraint epics_pkey primary key (id),
    constraint fk_epics_executor_id_users foreign key (executor_id) references users (id),
    constraint fk_epics_reporter_id_users foreign key (reporter_id) references users (id),
    constraint fk_epics_backlog_id_backlog foreign key (backlog_id) references backlog (id)
        on delete cascade,
    constraint fk_epics_sprint_id_sprint foreign key (sprint_id) references sprint (id)
        on delete cascade
);

create index idx_epics_creation_date on epics (creation_date);
create index idx_epics_name on epics (name);
create index idx_epics_priority on epics (priority);
create index idx_epics_status on epics (status);
create index idx_epics_executor_id on epics (executor_id);
create index idx_epics_reporter_id on epics (reporter_id);

create table stories
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      integer not null,
    status        varchar(255) not null,
    executor_id   integer,
    reporter_id   integer,
    backlog_id    integer,
    root_bug_id   integer,
    root_epic_id  integer,
    root_task_id  integer,
    sprint_id     integer,
    constraint stories_pkey primary key (id),
    constraint fk_stories_executor_id_users foreign key (executor_id) references users (id),
    constraint fk_stories_reporter_id_users foreign key (reporter_id) references users (id),
    constraint fk_stories_backlog_id_backlog foreign key (backlog_id) references backlog (id)
        on delete cascade,
    constraint fk_stories_sprint_id_sprint foreign key (sprint_id) references sprint (id)
        on delete cascade
);

create index idx_stories_creation_date on stories (creation_date);
create index idx_stories_name on stories (name);
create index idx_stories_priority on stories (priority);
create index idx_stories_status on stories (status);
create index idx_stories_executor_id on stories (executor_id);
create index idx_stories_reporter_id on stories (reporter_id);

create table tasks
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      integer not null,
    status        varchar(255) not null,
    executor_id   integer,
    reporter_id   integer,
    backlog_id    integer,
    root_bug_id   integer,
    root_epic_id  integer,
    root_story_id integer,
    sprint_id     integer,
    constraint tasks_pkey primary key (id),
    constraint fk_tasks_executor_id_users foreign key (executor_id) references users (id),
    constraint fk_tasks_reporter_id_users foreign key (reporter_id) references users (id),
    constraint fk_tasks_backlog_id_backlog foreign key (backlog_id) references backlog (id)
        on delete cascade,
    constraint fk_tasks foreign key (sprint_id) references sprint (id)
        on delete cascade
);

create index idx_tasks_creation_date on tasks (creation_date);
create index idx_tasks_name on tasks (name);
create index idx_tasks_priority on tasks (priority);
create index idx_tasks_status on tasks (status);
create index idx_tasks_executor_id on tasks (executor_id);
create index idx_tasks_reporter_id on tasks (reporter_id);

alter table bugs add constraint fk_bugs_root_epic_id_epics foreign key (root_epic_id) references epics (id);
alter table bugs add constraint fk_bugs_root_story_id_stories foreign key (root_story_id) references stories (id);
alter table bugs add constraint fk_bugs_root_task_id_tasks foreign key (root_task_id) references tasks (id);

alter table epics add constraint fk_epics_root_bug_id_bugs foreign key (root_bug_id) references bugs (id);
alter table epics add constraint fk_epics_root_story_id_stories foreign key (root_story_id) references stories (id);
alter table epics add constraint fk_epics_task_id_tasks foreign key (root_task_id) references tasks (id);

alter table stories add constraint fk_stories_root_bug_id_bugs foreign key (root_bug_id) references bugs (id);
alter table stories add constraint fk_stories_root_epic_id_epics foreign key (root_epic_id) references epics (id);
alter table stories add constraint fk_stories_task_id_tasks foreign key (root_task_id) references tasks (id);

alter table tasks add constraint fk_tasks_root_bug_id_bugs foreign key (root_bug_id) references bugs (id);
alter table tasks add constraint fk_tasks_root_epic_id_epics foreign key (root_epic_id) references epics (id);
alter table tasks add constraint fk_tasks_root_story_id_stories foreign key (root_story_id) references stories (id);