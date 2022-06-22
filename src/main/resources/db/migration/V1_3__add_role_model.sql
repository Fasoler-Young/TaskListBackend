create TABLE IF NOT EXISTS "user"(
    id integer primary key GENERATED ALWAYS AS IDENTITY,
    login varchar(30) UNIQUE not null,
    password varchar(100) not null,
    role varchar(20) not null default 'ROLE_USER',
    check (role = 'ROLE_USER' or role = 'ROLE_ADMIN' )
);
-- -- Добавляем неудаляемого пользователя по умолчанию
-- insert into "user" (login, password, role)
--     VALUES ('admin', '12345', 'ROLE_ADMIN');

create rule undeletable_user as on delete
    to "user" where id = 1
    do instead nothing;

-- Теперь задачи, категории, приоритеты и общая статистика
-- будут привязываться к конкретному пользователю
-- (При рефакторинге возможно следует отказаться от значения по умолчанию)
alter table task add column user_id integer not null;
alter table category add column user_id integer not null;
alter table stat add column user_id integer not null;
alter table priority add column user_id integer not null;

alter table stat alter column uncompleted_total set not null;
alter table stat alter column completed_total set not null;

alter table stat alter column uncompleted_total set default 0;
alter table stat alter column completed_total set default 0;

alter table category add constraint unique_category_for_user unique(user_id, title);
alter table priority add constraint unique_priority_for_user unique(user_id, title);

alter table stat add constraint unique_stat_for_user unique(user_id);




-- При добавлении нового пользователя
-- создаются поля статистики и категория по умолчанию
create function user_after_insert() returns trigger
    language plpgsql
as
$$
BEGIN
    insert into category (title, user_id) values ('No category', new.id);
    insert into stat(user_id) values (new.id);
    return new;
END;
$$;

create trigger user_after_insert
    after insert
    on "user"
    for each row
    execute procedure user_after_insert();


-- Переписываем правило для сохранения категории по умолчанию
-- оно позволит удалить категорию только после удаления пользователя
-- для этого будет создан триггер
create or replace rule undeletable_default_category as on delete
to category where old.title = 'No category' and exists(select 1 from "user" u
    where u.id = old.user_id)
do instead nothing;

-- Аналогично создадим правило для статистики и триггер
create or replace rule undeletable_stat as on DELETE
to stat where exists(select 1 from "user" u where u.id = old.user_id)
do instead nothing;


-- При удалении пользователя удаляются все данные
create function user_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    delete from task where user_id = old.id;
    delete from category where user_id = old.id;
    delete from priority where user_id = old.id;
    delete from stat where user_id = old.id;
    return old;
END;
$$;

create trigger user_after_delete
    after delete
    on "user"
    for each row
execute procedure user_after_delete();


-- Следует изменить старые триггеры для категорий задач и приоритетов
-- для корректного сбора информации, а также
-- для предотвращения лишних действий при удалении пользователя
create or replace function task_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    if exists(select 1 from "user" u where u.id = old.user_id) then
        if old.category_id is not null then
            if old.completed then
                update category set completed_count = completed_count - 1 where id = old.category_id;
            else
                update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
            end if;
        end if;

        if old.completed then
            update stat set completed_total = completed_total - 1 where user_id = old.user_id;
        else
            update stat set uncompleted_total = uncompleted_total - 1 where user_id = old.user_id;
        end if;
    end if;
    return old;
end;
$$;


create or replace function priority_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    if exists(select 1 from "user" u  where u.id = old.user_id) then
        update task set priority_id = null where priority_id = old.id;
    end if;
    return old;
end;
$$;

create or replace function category_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    if exists(select 1 from "user" u  where u.id = old.user_id) then
        update task set category_id = default where category_id = old.id;
    end if;
    return old;
end;
$$;

create or replace function task_after_insert() returns trigger
    language plpgsql
as
$$
begin
    if new.completed then
        update stat set completed_total = completed_total + 1 where user_id = new.user_id;
        update category set completed_count = completed_count + 1 where id = new.category_id;
    else
        update stat set uncompleted_total = uncompleted_total + 1 where user_id = new.user_id;
        update category set uncompleted_count = uncompleted_count + 1 where id = new.category_id;
    end if;
    return new;
end;
$$;

create or replace function task_after_update() returns trigger
    language plpgsql
as
$$
BEGIN
    if new.completed != old.completed then
        if new.completed then
            update stat set completed_total = completed_total + 1,
                            uncompleted_total = uncompleted_total -1 where user_id = new.user_id;
        else
            update stat set completed_total = completed_total - 1,
                            uncompleted_total = uncompleted_total + 1 where user_id = new.user_id;
        end if;
    end if;
    if new.category_id != old.category_id and new.completed != old.completed then
        if new.completed then
            update category set completed_count = completed_count + 1 where id = new.category_id;
            update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
        else
            update category set uncompleted_count = uncompleted_count + 1 where id = new.category_id;
            update category set completed_count = completed_count - 1 where id = old.category_id;
        end if;
    elseif new.category_id != old.category_id then
        if new.completed then
            update category set completed_count = completed_count - 1 where id = old.category_id;
            update category set completed_count = completed_count + 1 where id = new.category_id;
        else
            update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
            update category set uncompleted_count = uncompleted_count + 1 where id = new.category_id;
        end if;
    elseif new.completed != old.completed then
        if new.completed then
            update category set completed_count = completed_count + 1,
                                uncompleted_count = uncompleted_count - 1 where id = new.category_id;
        else
            update category set uncompleted_count = uncompleted_count + 1,
                                completed_count = completed_count - 1 where id = new.category_id;
        end if;
    else
    end if;

    return new;
end;
$$;
