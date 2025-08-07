insert into `user`(`id`, `username`, `password`, `role`, `user_uuid`)
values ('f2941987-1f60-4ae9-a33f-a767b9d07dff', 'admin', '6700221bbe32dbc80ba9b573f57ef75d', 'ADMIN', null);
insert into `user`(`id`, `username`, `password`, `role`, `user_uuid`)
values ('51269c36-1da8-4154-b6c1-4c9d22613345', 'testProf', 'b3d6069bc82a64da99c5d07d16b9984b', 'PROFESSOR',
        'b3dbda61-412e-4df8-93c3-6bc02b6f91f1');
insert into `user`(`id`, `username`, `password`, `role`, `user_uuid`)
values ('afa0cd75-5940-42d7-a8ca-89e8ba9320fb', 'testStud', '26566cb79d7f81196e1ceeef596840aa', 'STUDENT',
        '20b8f744-2d28-417c-a917-1acf0ffbb386');
insert into `student` (`id`, `jmbag`, `first_name`, `last_name`, `major`)
values ('20b8f744-2d28-417c-a917-1acf0ffbb386', '0246108773', 'Student', 'Test', 'SOFTWARE_ENGINEERING');
insert into `professor` (`id`, `first_name`, `last_name`)
values ('b3dbda61-412e-4df8-93c3-6bc02b6f91f1', 'Professor', 'Test');
insert into `course` (`ects`, `id`, `professor`, `name`)
values (7, '4bc9f0ab-c87e-4473-a3ec-192ab8652d41',
		'b3dbda61-412e-4df8-93c3-6bc02b6f91f1', 'Programiranje u jeziku Java');
insert into `grade` (`grade`, `course`, `id`, `student`)
values (5, '4bc9f0ab-c87e-4473-a3ec-192ab8652d41',
        'd5eb0ebb-0e76-44e8-bc40-de76f19a88eb', '20b8f744-2d28-417c-a917-1acf0ffbb386');
