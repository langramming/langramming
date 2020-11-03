CREATE TABLE hibernate_sequence
(
    next_val bigint not null
);
INSERT INTO hibernate_sequence (next_val) VALUES (1);

CREATE TABLE album_v1
(
    id                bigint primary key    not null,
    name              varchar(255)          not null,
    provider          varchar(255)          not null,
    provider_album_id varchar(255)          not null
);

CREATE TABLE artist_v1
(
    id                 bigint primary key   not null,
    name               varchar(255)         not null,
    provider           varchar(255)         not null,
    provider_artist_id varchar(255)         not null
);

CREATE TABLE language_v1
(
    id   bigint primary key not null,
    code varchar(255)       not null unique,
    name varchar(255)       not null unique
);

CREATE TABLE spotify_user_v1
(
    id            bigint primary key    not null,
    access_token  varchar(255)          not null,
    expires_at    bigint                not null,
    refresh_token varchar(255)          not null,
    scope         varchar(255)          not null,
    token_type    varchar(255)          not null,
    user_id       bigint                not null unique
);

CREATE TABLE track_artists_v1
(
    track_id  bigint not null,
    artist_id bigint not null
);

CREATE TABLE track_image_v1
(
    id       bigint primary key not null,
    height   integer,
    url      varchar(255),
    width    integer,
    track_id bigint
);

CREATE TABLE track_language_v1
(
    id           bigint primary key not null,
    language_id  bigint             not null,
    tagged_by_id bigint             not null,
    track_id     bigint             not null
);

CREATE TABLE track_v1
(
    id                bigint primary key    not null,
    name              varchar(255)          not null,
    provider          varchar(255)          not null,
    provider_track_id varchar(255)          not null,
    url               varchar(255)          not null,
    album             bigint
);

CREATE TABLE user_v1
(
    id          bigint primary key  not null,
    name        varchar(255)        not null,
    telegram_id bigint              not null unique
);