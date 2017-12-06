create table pantry_mapping(
	id int(11) not null auto_increment,
	res_emp_id int(11),
	resd_emp_id int(11),
	res_date bigint(20),
	start_time bigint(20),
	end_time bigint(20),
	d_tag varchar(1),
	primary key (id),
	foreign key (res_emp_id) references employee(id),
	foreign key (resd_emp_id) references employee(id)
);
