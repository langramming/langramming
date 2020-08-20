CREATE TABLE album_v1
(
    id                bigint       not null,
    name              varchar(255) not null,
    provider          varchar(255) not null,
    provider_album_id varchar(255) not null,
    primary key (id)
);

CREATE TABLE artist_v1
(
    id                 bigint       not null,
    name               varchar(255) not null,
    provider           varchar(255) not null,
    provider_artist_id varchar(255) not null,
    primary key (id)
);

CREATE TABLE language_v1
(
    id   bigint       not null,
    code varchar(255) not null unique,
    name varchar(255) not null unique,
    primary key (id)
);

CREATE TABLE spotify_user_v1
(
    id            bigint       not null,
    access_token  varchar(255) not null,
    expires_at    bigint       not null,
    refresh_token varchar(255) not null,
    scope         varchar(255) not null,
    token_type    varchar(255) not null,
    user_id       bigint       not null unique,
    primary key (id)
);

CREATE TABLE track_artists_v1
(
    track_id  bigint not null,
    artist_id bigint not null
);

CREATE TABLE track_image_v1
(
    id       bigint not null,
    height   integer,
    url      varchar(255),
    width    integer,
    track_id bigint,
    primary key (id)
);

CREATE TABLE track_language_v1
(
    id           bigint not null,
    language_id  bigint not null,
    tagged_by_id bigint not null,
    track_id     bigint not null,
    primary key (id)
);

CREATE TABLE track_v1
(
    id                bigint       not null,
    name              varchar(255) not null,
    provider          varchar(255) not null,
    provider_track_id varchar(255) not null,
    url               varchar(255) not null,
    album             bigint,
    primary key (id)
);

CREATE TABLE user_v1
(
    id          bigint       not null,
    name        varchar(255) not null,
    telegram_id bigint       not null unique,
    primary key (id)
);