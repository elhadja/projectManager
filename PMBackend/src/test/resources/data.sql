insert into user_account (id, pseudo, email) values (1, "elhadj1", "email1@test.com");
insert into user_account (id, pseudo, email) values (2, "elhadj2", "email2@test.com");
insert into user_account (id, pseudo, email) values (3, "elhadj3", "email3@test.com");
insert into user_account (id, pseudo, email) values (4, "elhadj4", "email4@test.com");

insert into project (id, name) values (1, "project1");

insert into user_project (user_id, project_id) 
				values (1, 1), (2, 1), (3, 1), (4, 1);

insert into project_managers (managed_projects_id, managers_id) values (1, 1);
insert into project_managers (managed_projects_id, managers_id) values (1, 2);