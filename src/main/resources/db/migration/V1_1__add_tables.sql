create TABLE IF NOT EXISTS task (
    id integer primary key GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(100) NOT NULL,
    priority_id INTEGER,
    completed BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS category(
    id INTEGER primary key GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(100) NOT NULL,
    completed_count INTEGER,
    uncompleted_count INTEGER
);

CREATE TABLE IF NOT EXISTS priority(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(45) NOT NULL,
    color VARCHAR(45)
);

CREATE TABLE IF NOT EXISTS stat(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    completed_total INTEGER,
    uncompleted_total INTEGER
);