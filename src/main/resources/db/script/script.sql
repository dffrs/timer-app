create table User
(
    user_id   int auto_increment
        primary key,
    user_name varchar(40) not null
)
    comment 'Info about the user';

create table Project
(
    project_id          int auto_increment
        primary key,
    project_name        varchar(50)  not null,
    project_description varchar(200) null,
    user_id             int          null,
    constraint Project_project_name_uindex
        unique (project_name),
    constraint Project_User_user_id_fk
        foreign key (user_id) references User (user_id)
            on update cascade on delete cascade
)
    comment 'Info about the all projects saved in this db';

create table Working_Time
(
    starting_time datetime default CURRENT_TIMESTAMP not null,
    time_spent    time                               not null,
    project_id    int                                not null,
    constraint Working_Time_Project_project_id_fk
        foreign key (project_id) references Project (project_id)
            on update cascade on delete cascade
)
    comment 'Info about time spent working on a project, including its initial date.';

create
    definer = ultra@`%` procedure DeleteRecords()
BEGIN

    DELETE FROM User;
    DELETE FROM Project;
    DELETE FROM Working_Time;

END;

grant execute, alter routine on procedure DeleteRecords to ultra;

create
    definer = ultra@`%` procedure GetUserTimeSpentPerProject(IN user_name varchar(40))
BEGIN
    SELECT User.user_name, Project.project_name, Working_Time.starting_time, Working_Time.time_spent
    FROM User,
         Working_Time,
         Project
    WHERE Working_Time.project_id = Project.project_id
      AND Project.user_id = User.user_id
      AND User.user_name = user_name;
END;

grant execute, alter routine on procedure GetUserTimeSpentPerProject to ultra;

create
    definer = ultra@`%` procedure InsertNewProject(IN projectName varchar(50), IN projectDescription varchar(200),
                                                   IN user varchar(40))
BEGIN
    DECLARE u_id INT DEFAULT -1;

    IF NOT EXISTS(SELECT timer_app.User.user_id FROM timer_app.User WHERE user = timer_app.User.user_name) THEN
        CALL InsertNewUser(user);
    END IF;

    SELECT timer_app.User.user_id INTO u_id FROM timer_app.User WHERE timer_app.User.user_name = user;

    INSERT INTO Project(project_id, project_name, project_description, user_id)
    VALUES (NULL, projectName, projectDescription, u_id);

END;

grant execute, alter routine on procedure InsertNewProject to ultra;

create
    definer = ultra@`%` procedure InsertNewUser(IN userName varchar(40))
BEGIN

    INSERT INTO User(user_id, user_name)
        VALUE (NULL, userName);

    SELECT CONCAT(userName, ' was inserted.') as Response;

END;

grant execute, alter routine on procedure InsertNewUser to ultra;

create
    definer = ultra@`%` procedure SaveTime(IN projectName varchar(50), IN projectDescription varchar(200),
                                           IN user varchar(40), IN timeSpent time)
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

grant execute, alter routine on procedure SaveTime to ultra;


