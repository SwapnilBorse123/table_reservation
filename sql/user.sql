create table user(
	id int(11) auto_increment,
	emp_first_name varchar(255),
	emp_last_name varchar(255),
	emp_id int(11),
	emp_email varchar(255),
	emp_password  varchar(255),
	primary key (id)
);

insert into user (emp_first_name,emp_last_name,emp_id,emp_email,emp_password) values ('Swapnil','Borse',393,'swapnil.borse@decimalpointanalytics.com','$2a$10$SNk5bj6H7V0ey81zI.CxQujerO6E98HccFZDmVbnY02hG/Wvkyabi');

insert into user (emp_first_name,emp_last_name,emp_id,emp_email,emp_password) values ('Kedar','Naik',424,'kedar.naik@decimalpointanalytics.com','$2a$10$JMM.CbwM5DcS/LO65fI5F.me2L7UizMYEctWcYkr5Zx7lyqq97xgm');

insert into user (emp_first_name,emp_last_name,emp_id,emp_email,emp_password) values ('Janvi','Parikh',369,'janvi.parikh@decimalpointanalytics.com','$2a$10$jS4ENyVrNa40hrJntaDQgOipzweembTYBxCrKARP21yUyh/CF0eKm');

