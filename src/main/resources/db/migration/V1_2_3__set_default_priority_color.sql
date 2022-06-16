ALTER TABLE priority
    alter column color set default '#ffffff';

ALTER TABLE category
    alter column completed_count set not null,
    alter column uncompleted_count set not null;