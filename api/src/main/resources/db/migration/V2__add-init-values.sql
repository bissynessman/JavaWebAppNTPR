INSERT INTO `user` (
                `id`,
                `role`,
                `user_uuid`,
                `username`,
                `password`
			)
     VALUES (
                'f2941987-1f60-4ae9-a33f-a767b9d07dff',
                'ADMIN',
                null,
                'admin',
                '6700221bbe32dbc80ba9b573f57ef75d'
            );
INSERT INTO `user` (
                `id`,
                `role`,
                `user_uuid`,
                `username`,
                `password`
			)
     VALUES (
                '51269c36-1da8-4154-b6c1-4c9d22613345',
                'PROFESSOR',
                'b3dbda61-412e-4df8-93c3-6bc02b6f91f1',
                'testProf',
                'b3d6069bc82a64da99c5d07d16b9984b'
            );
INSERT INTO `user` (
                `id`,
                `role`,
                `user_uuid`,
                `username`,
                `password`
			)
	 VALUES (
	            'afa0cd75-5940-42d7-a8ca-89e8ba9320fb',
	            'STUDENT',
	            '20b8f744-2d28-417c-a917-1acf0ffbb386',
	            'testStud',
	            '26566cb79d7f81196e1ceeef596840aa'
	        );
INSERT INTO `student` (
                `id`,
                `jmbag`,
                `first_name`,
                `last_name`,
                `major`
			)
	 VALUES (
	            '20b8f744-2d28-417c-a917-1acf0ffbb386',
	            '0246108773',
	            'Student',
	            'Test',
	            'SOFTWARE_ENGINEERING'
	        );
INSERT INTO `professor` (
                `id`,
                `first_name`,
                `last_name`
			)
	 VALUES (
                'b3dbda61-412e-4df8-93c3-6bc02b6f91f1',
	            'Professor',
	            'Test'
	        );
INSERT INTO `course` (
				`id`,
				`name`,
                `ects`,
                `professor`
			)
	 VALUES (
		        '4bc9f0ab-c87e-4473-a3ec-192ab8652d41',
		        'Programiranje u jeziku Java',
	            7,
				'b3dbda61-412e-4df8-93c3-6bc02b6f91f1'
	        );
INSERT INTO `grade` (
				`id`,
                `course`,
                `student`,
				`grade`
			)
	 VALUES (
		        'd5eb0ebb-0e76-44e8-bc40-de76f19a88eb',
	            '4bc9f0ab-c87e-4473-a3ec-192ab8652d41',
	            '20b8f744-2d28-417c-a917-1acf0ffbb386',
			    5
	        );
