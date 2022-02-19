insert into user_account (id, pseudo, email) values (1, "elhadj1", "email1@test.com");
insert into user_account (id, pseudo, email) values (2, "elhadj2", "email2@test.com");

insert into project (id, name) values (1, "project1");

insert into user_project (user_id, project_id) 
				values (1, 1);

insert into project_managers (managed_projects_id, managers_id) values (1, 1);

insert into invitation_to_project (id, author_id, guest_id, project_id)
	values (1, 1, 2, 1);