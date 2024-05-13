CREATE TABLE users
(
    id         INTEGER  PRIMARY KEY,
    username   VARCHAR(32) UNIQUE NOT NULL,
    email      VARCHAR(32) UNIQUE NOT NULL,
    password   VARCHAR(255)       NOT NULL,
    first_name VARCHAR(32) DEFAULT ' ',
    last_name  VARCHAR(32)  DEFAULT ' ',
    birth_date DATE DEFAULT CURRENT_DATE ,
    info       TEXT  DEFAULT ' ',
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE TABLE tags
(
    id    INTEGER  PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);
CREATE TABLE articles
(
    author_id  INTEGER  REFERENCES users (id) NOT NULL,
    id          INTEGER  PRIMARY KEY,
    title      VARCHAR(128) UNIQUE        NOT NULL,
    content    TEXT                       NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE comments
(
    id          INTEGER  PRIMARY KEY ,
    author_id  INTEGER REFERENCES users (id)      NOT NULL,
    article_id INTEGER REFERENCES articles (id) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    content    TEXT                            NOT NULL
);


CREATE TABLE article_tags
(
    article_id INTEGER REFERENCES articles (id) NOT NULL,
    tag_id     INTEGER REFERENCES tags (id)     NOT NULL,
    PRIMARY KEY (article_id, tag_id)
);
