-- Project: table
CREATE TABLE `Project`
(
    `project_id`          int         NOT NULL AUTO_INCREMENT,
    `project_name`        varchar(50) NOT NULL,
    `project_description` varchar(200) DEFAULT NULL,
    `user_id`             int          DEFAULT NULL,
    PRIMARY KEY (`project_id`),
    UNIQUE KEY `Project_project_name_uindex` (`project_name`),
    KEY `Project_User_user_id_fk` (`user_id`),
    CONSTRAINT `Project_User_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 23
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Info about the all projects saved in this db';

-- No native definition for element: Project_User_user_id_fk (index)

-- User: table
CREATE TABLE `User`
(
    `user_id`   int         NOT NULL AUTO_INCREMENT,
    `user_name` varchar(40) NOT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Info about the user';

-- Working_Time: table
CREATE TABLE `Working_Time`
(
    `starting_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `time_spent`    time     NOT NULL,
    `project_id`    int      NOT NULL,
    KEY `Working_Time_Project_project_id_fk` (`project_id`),
    CONSTRAINT `Working_Time_Project_project_id_fk` FOREIGN KEY (`project_id`) REFERENCES `Project` (`project_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Info about time spent working on a project, including its initial date.';

-- No native definition for element: Working_Time_Project_project_id_fk (index)

-- InsertNewProject(varchar(50),varchar(200),varchar(40)): routine
CREATE
    DEFINER = `ultra`@`localhost` PROCEDURE `InsertNewProject`(
    IN projectName VARCHAR(50),
    IN projectDescription VARCHAR(200),
    IN user VARCHAR(40)
)
BEGIN
    DECLARE u_id INT DEFAULT -1;

    IF NOT EXISTS(SELECT timer_app.User.user_id FROM timer_app.User WHERE user = timer_app.User.user_name) THEN
        CALL InsertNewUser(user);
    END IF;

    SELECT timer_app.User.user_id INTO u_id FROM timer_app.User WHERE timer_app.User.user_name = user;

    INSERT INTO Project(project_id, project_name, project_description, user_id)
    VALUES (NULL, projectName, projectDescription, u_id);

END;

-- InsertNewUser(varchar(40)): routine
CREATE
    DEFINER = `ultra`@`localhost` PROCEDURE `InsertNewUser`(
    IN userName VARCHAR(40)
)
BEGIN

    INSERT INTO User(user_id, user_name)
        VALUE (NULL, userName);

    SELECT CONCAT(userName, ' was inserted.') as Response;

END;

-- SaveTime(varchar(50),varchar(200),varchar(40),time): routine
CREATE
    DEFINER = `ultra`@`localhost` PROCEDURE `SaveTime`(
    IN projectName VARCHAR(50),
    IN projectDescription VARCHAR(200),
    IN user VARCHAR(40),
    IN timeSpent TIME
)
BEGIN

    DECLARE p_id INT DEFAULT -1;

    IF NOT EXISTS(SELECT timer_app.Project.project_id
                  FROM timer_app.Project
                  WHERE timer_app.Project.project_name = projectName) THEN
        CALL InsertNewProject(projectName, projectDescription, user);
    END IF;

    SELECT timer_app.Project.project_id
    INTO p_id
    FROM timer_app.Project
    WHERE timer_app.Project.project_name = projectName;

    INSERT INTO Working_Time(time_spent, project_id)
    VALUES (timeSpent, p_id);

END;


