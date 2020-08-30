--liquibase formatted sql
/* https://www.liquibase.org/documentation/sql_format.html */

--changeset dev:1
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

--changeset dev:2
create sequence global_seq start with 100000;
--rollback drop sequence if exists global_seq;

--changeset dev:3
create table users
(
    id       integer default nextval('global_seq'),
    email    varchar(255) not null,
    name     varchar(255) not null,
    password varchar(255) not null,
    constraint users_pkey primary key (id),
    constraint uk_email unique (email)
);

--changeset dev:4
create table user_roles
(
    user_id integer not null,
    roles   varchar(255),
    constraint fk_user_roles_user_id_users foreign key (user_id) references users (id)
        on delete cascade
);
--rollback drop table if exists user_roles;

--changeset dev:5
create table backlog
(
    id integer default nextval('global_seq'),
    constraint backlog_pkey primary key (id)
);
--rollback drop table if exists backlog;

--changeset dev:6
create table sprint
(
    id integer default nextval('global_seq'),
    constraint sprint_pkey primary key (id)
);
--rollback drop table if exists sprint;

--changeset dev:7
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
--rollback drop table if exists projects;

--changeset dev:8
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
--rollback drop table if exists project_users;

--changeset dev:9
create table bugs
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      varchar(10) not null,
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
--rollback drop table if exists bugs cascade;

--changeset dev:10
create table epics
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      varchar(10) not null,
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
--rollback drop table if exists epics cascade;

--changeset dev:11
create table stories
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      varchar(10) not null,
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
--rollback drop table if exists stories cascade;

--changeset dev:12
create table tasks
(
    id            integer default nextval('global_seq'),
    creation_date timestamp not null,
    name          varchar(255) not null,
    priority      varchar(10) not null,
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
--rollback drop table if exists tasks cascade;

--changeset dev:13
alter table bugs add constraint fk_bugs_root_epic_id_epics foreign key (root_epic_id) references epics (id) on delete cascade;
--rollback alter table bugs drop constraint fk_bugs_root_epic_id_epics;

--changeset dev:14
alter table bugs add constraint fk_bugs_root_story_id_stories foreign key (root_story_id) references stories (id) on delete cascade;
--rollback alter table bugs drop constraint fk_bugs_root_story_id_stories;

--changeset dev:15
alter table bugs add constraint fk_bugs_root_task_id_tasks foreign key (root_task_id) references tasks (id) on delete cascade;
--rollback alter table bugs drop constraint fk_bugs_root_task_id_tasks;

--changeset dev:16
alter table epics add constraint fk_epics_root_bug_id_bugs foreign key (root_bug_id) references bugs (id) on delete cascade;
--rollback alter table epics drop constraint fk_epics_root_bug_id_bugs;

--changeset dev:17
alter table epics add constraint fk_epics_root_story_id_stories foreign key (root_story_id) references stories (id) on delete cascade;
--rollback alter table epics drop constraint fk_epics_root_story_id_stories;

--changeset dev:18
alter table epics add constraint fk_epics_task_id_tasks foreign key (root_task_id) references tasks (id) on delete cascade;
--rollback alter table epics drop constraint fk_epics_task_id_tasks;

--changeset dev:19
alter table stories add constraint fk_stories_root_bug_id_bugs foreign key (root_bug_id) references bugs (id) on delete cascade;
--rollback alter table stories drop constraint fk_stories_root_bug_id_bugs;

--changeset dev:20
alter table stories add constraint fk_stories_root_epic_id_epics foreign key (root_epic_id) references epics (id) on delete cascade;
--rollback alter table stories drop constraint fk_stories_root_epic_id_epics;

--changeset dev:21
alter table stories add constraint fk_stories_task_id_tasks foreign key (root_task_id) references tasks (id) on delete cascade;
--rollback alter table stories drop constraint fk_stories_task_id_tasks;

--changeset dev:22
alter table tasks add constraint fk_tasks_root_bug_id_bugs foreign key (root_bug_id) references bugs (id) on delete cascade;
--rollback alter table tasks drop constraint fk_tasks_root_bug_id_bugs;

--changeset dev:23
alter table tasks add constraint fk_tasks_root_epic_id_epics foreign key (root_epic_id) references epics (id) on delete cascade;
--rollback alter table tasks drop constraint fk_tasks_root_epic_id_epics;

--changeset dev:24
alter table tasks add constraint fk_tasks_root_story_id_stories foreign key (root_story_id) references stories (id) on delete cascade;
--rollback alter table tasks drop constraint fk_tasks_root_story_id_stories;

--changeset dev:25
--comment: restart sequence to 100_000
ALTER SEQUENCE global_seq RESTART WITH 100000;
--rollback ALTER SEQUENCE global_seq RESTART WITH 100000;

--changeset dev:26
--comment: insert first users
INSERT INTO users(email, name, password) VALUES
('user@ya.ru', 'user', '{tracker}$2a$10$rbIG.Yd0xNbO7/2R85YdI.dngz6L4uxwv3Hfv9aAZDarrS3raLpRO'),
('test@inbox.ru', 'tester', '{tracker}$2a$10$gXpVDAGU./O86fFcmIOJI.97MLTF8Hqf7qDC9QXzfBg0HOgM4rOMS'),
('hello@gmail.ru', 'worker', '{tracker}$2a$10$LnV17obOMWVPCkUIaXSsH.bF2Nn2DbSzhArqKe09p8aNiY/yQpDIa');
--rollback DELETE FROM users;

--changeset dev:27
INSERT INTO user_roles(user_id, roles) VALUES
(100000, 'ROLE_USER'),
(100000, 'ROLE_ADMIN'),
(100001, 'ROLE_USER'),
(100002, 'ROLE_USER');
--rollback DELETE FROM user_roles;

--changeset dev:28
INSERT INTO backlog DEFAULT VALUES;
INSERT INTO backlog DEFAULT VALUES;
--rollback DELETE FROM backlog;

--changeset dev:29
INSERT INTO sprint DEFAULT VALUES;
INSERT INTO sprint DEFAULT VALUES;
--rollback DELETE FROM sprint;

--changeset dev:30
INSERT INTO projects(administrator, department, description, manager, name, backlog_id, sprint_id) VALUES
('user@ya.ru', 'google ml', 'machine learning', 'hello@gmail.ru', 'Google ML', 100003, 100005),
('test@inbox.ru', 'Apple Inc', 'Apple VR', 'test@inbox.ru', 'Apple VR', 100004, 100006);
--rollback DELETE FROM projects;

--changeset dev:31
INSERT INTO project_users(project_id, user_id) VALUES
(100007, 100000),
(100007, 100002),
(100008, 100000),
(100008, 100001);
--rollback DELETE FROM project_users;

--changeset dev:32
INSERT INTO bugs(creation_date, name, priority, status, executor_id, reporter_id, backlog_id,
                 root_epic_id, root_story_id, root_task_id, sprint_id) VALUES
('2020-05-20 10:00:00', 'bug1', 'LOW', 'OPEN_ISSUE', 100001, 100000, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'bug2', 'MEDIUM', 'REVIEW_ISSUE', 100000, 100001, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'bugSprint', 'MEDIUM', 'RESOLVED_ISSUE', 100000, 100001, NULL, NULL, NULL, NULL, 100005),
('2020-05-23 15:00:00', 'bug3', 'MEDIUM', 'TEST_ISSUE', 100002, 100000, 100004, NULL, NULL, NULL, NULL);
--rollback DELETE FROM bugs;

--changeset dev:33
INSERT INTO epics(creation_date, name, priority, status, executor_id, reporter_id, backlog_id,
                  root_bug_id, root_story_id, root_task_id, sprint_id) VALUES
('2020-04-20 10:00:00', 'epic1', 'LOW', 'OPEN_ISSUE', 100001, 100000, 100003, NULL, NULL, NULL, NULL),
('2020-06-23 15:00:00', 'epic2', 'MEDIUM', 'REVIEW_ISSUE', 100000, 100001, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'epicSprint', 'MEDIUM', 'RESOLVED_ISSUE', 100000, 100001, NULL, NULL, NULL, NULL, 100005),
('2020-05-25 15:00:00', 'epic3', 'MEDIUM', 'TEST_ISSUE', 100002, 100000, 100004, NULL, NULL, NULL, NULL);
--rollback DELETE FROM epics;