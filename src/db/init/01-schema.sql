DROP TABLE IF EXISTS name_basics;
DROP TABLE IF EXISTS title_basics;
DROP TABLE IF EXISTS title_episodes;
DROP TABLE IF EXISTS title_principals;
DROP TABLE IF EXISTS title_ratings;

CREATE TABLE name_basics (
    nconst VARCHAR(10) PRIMARY KEY,
    primary_name VARCHAR NOT NULL,
    birth_year SMALLINT,
    death_year SMALLINT,
    primary_profession VARCHAR,
    known_for_titles VARCHAR
);

CREATE TABLE title_basics (
    tconst VARCHAR(10) PRIMARY KEY,
    title_type VARCHAR(15) NOT NULL,
    primary_title VARCHAR NOT NULL,
    original_title VARCHAR NOT NULL,
    is_adult BOOLEAN DEFAULT FALSE,
    start_year SMALLINT,
    end_year SMALLINT,
    runtime_minutes INT,
    genres VARCHAR
);

CREATE INDEX title_basics_title_type_idx ON title_basics (title_type);
CREATE INDEX title_basics_primary_title_idx ON title_basics (primary_title);

CREATE TABLE title_episodes (
    tconst VARCHAR(10) PRIMARY KEY,
    parent_tconst VARCHAR(10) NOT NULL,
    season_number SMALLINT,
    episode_number INT
);

CREATE INDEX title_episodes_parent_tconst_idx ON title_episodes (parent_tconst);

CREATE TABLE title_principals (
    tconst VARCHAR(10),
    ordering SMALLINT,
    nconst VARCHAR(10) NOT NULL,
    category VARCHAR,
    job VARCHAR,
    characters VARCHAR,
    PRIMARY KEY(tconst, ordering)
);

CREATE TABLE title_ratings (
   tconst CHAR(10) PRIMARY KEY,
   average_rating REAL NOT NULL,
   num_votes INT NOT NULL
);

