create function task_after_update() returns trigger
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
    if new.category_id != old.category_id then
        if old.category_id is not null then
            if old.completed then
                update category set completed_count = completed_count - 1 where id = old.category_id;
            else
                update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
            end if;
        end if;
        if new.category_id is not null then
            if new.completed then
                update category set completed_count = completed_count + 1 where id = new.category_id;
            else
                update category set uncompleted_count = uncompleted_count + 1 where id = new.category_id;
            end if;
        end if;
    elseif new.category_id is not null and new.completed != old.completed and new.completed then
        update category set completed_count = completed_count + 1,
                            uncompleted_count = uncompleted_count - 1 where id = new.category_id;
    else
        update category set completed_count = completed_count - 1,
                            uncompleted_count = uncompleted_count + 1 where id = new.category_id;
    end if;
    return new;
end;
$$;


create function task_after_insert() returns trigger
    language plpgsql
as
$$
begin
    if new.category_id is not null then
        if new.completed then
            update category set completed_count = completed_count + 1 where id = new.category_id;
        else
            update category set uncompleted_count = uncompleted_count + 1 where id = new.category_id;
        end if;
    end if;

    if new.completed then
        update stat set completed_total = completed_total + 1 where id = 1;
    else
        update stat set uncompleted_total = uncompleted_total + 1 where id = 1;
    end if;
    return new;
end;
$$;


create function task_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    if old.category_id is not null then
        if old.completed then
            update category set completed_count = completed_count - 1 where id = old.category_id;
        else
            update category set uncompleted_count = uncompleted_count - 1 where id = old.category_id;
        end if;
    end if;

    if old.completed then
        update stat set completed_total = completed_total - 1 where id = 1;
    else
        update stat set uncompleted_total = uncompleted_total - 1 where id = 1;
    end if;
    return old;
end;
$$;


create function priority_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    update task set priority_id = null where priority_id = old.id;
    return old;
end;
$$;


create trigger task_after_delete
    after delete
    on task
    for each row
execute procedure task_after_delete();


create trigger task_after_insert
    after insert
    on task
    for each row
execute procedure task_after_insert();


create trigger task_after_update
    after update
    on task
    for each row
execute procedure task_after_update();
