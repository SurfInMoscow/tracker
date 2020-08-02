DELETE FROM projects;
DELETE FROM backlog;
DELETE FROM sprint;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

/*
 user - password
 tester - testpass
 worker - workerpass
  */
INSERT INTO users(email, name, password) VALUES
('user@ya.ru', 'user', '{tracker}$2a$10$rbIG.Yd0xNbO7/2R85YdI.dngz6L4uxwv3Hfv9aAZDarrS3raLpRO'),
('test@inbox.ru', 'tester', '{tracker}$2a$10$gXpVDAGU./O86fFcmIOJI.97MLTF8Hqf7qDC9QXzfBg0HOgM4rOMS'),
('hello@gmail.ru', 'worker', '{tracker}$2a$10$LnV17obOMWVPCkUIaXSsH.bF2Nn2DbSzhArqKe09p8aNiY/yQpDIa');

INSERT INTO user_roles(user_id, roles) VALUES
(100000, 'ROLE_USER'),
(100001, 'ROLE_USER'),
(100002, 'ROLE_USER');

INSERT INTO backlog DEFAULT VALUES;
INSERT INTO backlog DEFAULT VALUES;

INSERT INTO sprint DEFAULT VALUES;
INSERT INTO sprint DEFAULT VALUES;

INSERT INTO projects(administrator, department, description, manager, name, backlog_id, sprint_id) VALUES
('user@ya.ru', 'google ml', 'machine learning', 'hello@gmail.ru', 'Google ML', 100003, 100005),
('test@inbox.ru', 'Apple Inc', 'Apple VR', 'test@inbox.ru', 'Apple VR', 100004, 100006);

INSERT INTO project_users(project_id, user_id) VALUES
(100007, 100000),
(100007, 100002),
(100008, 100000),
(100008, 100001);

INSERT INTO bugs(creation_date, name, priority, status, executor_id, reporter_id, backlog_id,
                 root_epic_id, root_story_id, root_task_id, sprint_id) VALUES
('2020-05-20 10:00:00', 'bug1', 'LOW', 'OPEN_ISSUE', 100001, 100000, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'bug2', 'MEDIUM', 'REVIEW_ISSUE', 100000, 100001, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'bugSprint', 'MEDIUM', 'RESOLVED_ISSUE', 100000, 100001, NULL, NULL, NULL, NULL, 100005),
('2020-05-23 15:00:00', 'bug3', 'MEDIUM', 'TEST_ISSUE', 100002, 100000, 100004, NULL, NULL, NULL, NULL);

INSERT INTO epics(creation_date, name, priority, status, executor_id, reporter_id, backlog_id,
                  root_bug_id, root_story_id, root_task_id, sprint_id) VALUES
('2020-04-20 10:00:00', 'epic1', 'LOW', 'OPEN_ISSUE', 100001, 100000, 100003, NULL, NULL, NULL, NULL),
('2020-06-23 15:00:00', 'epic2', 'MEDIUM', 'REVIEW_ISSUE', 100000, 100001, 100003, NULL, NULL, NULL, NULL),
('2020-05-23 15:00:00', 'epicSprint', 'MEDIUM', 'RESOLVED_ISSUE', 100000, 100001, NULL, NULL, NULL, NULL, 100005),
('2020-05-25 15:00:00', 'epic3', 'MEDIUM', 'TEST_ISSUE', 100002, 100000, 100004, NULL, NULL, NULL, NULL);