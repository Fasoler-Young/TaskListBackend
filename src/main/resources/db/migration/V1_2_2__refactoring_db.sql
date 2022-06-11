-- Категория не может быть пустой, все сделано так, что всегда будет неудаляемая категория
alter table task add column category_id INTEGER not null default 1;


alter table task
    alter column completed set not null;


alter table category
    alter column uncompleted_count set default 0,
    alter column completed_count set default 0;


-- Создание базовых полей
insert into stat (completed_total, uncompleted_total) values (0, 0);
insert into category(title, completed_count, uncompleted_count) VALUES ('Default', 0, 0);


-- Переработка старого триггера, так как предыдущий неправильно обрабатывал случаи с нулевой категорией
create or replace function task_after_update() returns trigger
    language plpgsql
as
$$
BEGIN
    if new.completed != old.completed then
        if new.completed then
            update stat set completed_total = completed_total + 1,
                            uncompleted_total = uncompleted_total -1 where id = 1;
        else
            update stat set completed_total = completed_total - 1,
                            uncompleted_total = uncompleted_total + 1 where id = 1;
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


-- Теперь после удаления категории ставится категория по умолчанию,
-- а при запуске триггера обновления задач данные меняются и в таблице категорий
create or replace function category_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    update task set category_id = default where category_id = old.id;

    return old;
end;
$$;


create or replace function task_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    if old.completed then
        update category set completed_count = completed_count - 1 where id = old.category_id;
    else
        update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
    end if;

    if old.completed then
        update stat set completed_total = completed_total - 1 where id = 1;
    else
        update stat set uncompleted_total = uncompleted_total - 1 where id = 1;
    end if;
    return old;
end;
$$;
