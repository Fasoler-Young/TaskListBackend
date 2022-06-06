create function category_after_delete() returns trigger
    language plpgsql
as
$$
BEGIN
    update task set category_id = null where category_id = old.id;
    return old;
end;
$$;


create trigger category_after_delete
    after delete
    on category
    for each row
execute procedure category_after_delete();


create trigger priority_after_delete
    after delete
    on priority
    for each row
execute procedure priority_after_delete();


