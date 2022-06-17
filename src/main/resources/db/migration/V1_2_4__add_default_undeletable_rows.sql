create RULE undeletable_default_category as on DELETE
    to category where id = 1
    do INSTEAD NOTHING;

create RULE undeletable_stat as on DELETE
    to stat where id = 1
    do INSTEAD NOTHING;