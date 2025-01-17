create table badges
(
    id          int auto_increment
        primary key,
    created_at  datetime(6)  null,
    updated_at  datetime(6)  null,
    description varchar(255) null,
    logo_url    varchar(255) null,
    name        varchar(50)  null,
    constraint UKcuebofvgkgi4g9fxde2kmpr1h
        unique (name)
);

create table roles
(
    id         int auto_increment
        primary key,
    created_at datetime(6)                                        null,
    updated_at datetime(6)                                        null,
    role_name  enum ('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER') null,
    constraint UK716hgxp60ym1lifrdgp67xt5k
        unique (role_name)
);

create table tags
(
    id          int auto_increment
        primary key,
    description varchar(100) null,
    name        varchar(21)  null,
    tagged      int          not null
);

create table users
(
    id            int auto_increment
        primary key,
    created_at    datetime(6)  null,
    updated_at    datetime(6)  null,
    avatar_url    tinytext     null,
    email_hash    varchar(255) null,
    is_enabled    bit          not null,
    is_verified   bit          not null,
    password_hash varchar(255) null,
    provider      varchar(255) null,
    username      varchar(255) null,
    constraint UKnd4y21hk7e6tjfmoow5yh2i4b
        unique (email_hash),
    constraint UKq4gvg4dl2a3fpetfwspodde8e
        unique (email_hash, username),
    constraint UKr43af9ap4edm43mmtq01oddj6
        unique (username)
);

create table content_reports
(
    id          int auto_increment
        primary key,
    created_at  datetime(6) null,
    updated_at  datetime(6) null,
    comment_id  int         null,
    question_id int         null,
    user_id     int         null,
    constraint FKsjaxojr45k9lfp633vres6u84
        foreign key (user_id) references users (id)
);

create table following
(
    followed_id   int  not null,
    follower_id   int  not null,
    subscribed_at date null,
    primary key (followed_id, follower_id),
    constraint FK7o8rmeuf83dqi0b2bvdhuwo9g
        foreign key (follower_id) references users (id),
    constraint FKi1insai6ol7bolelj4rgfpkgs
        foreign key (followed_id) references users (id)
);

create table jwt_tokens
(
    id                int auto_increment
        primary key,
    device_name       varchar(255) null,
    expiration_before timestamp    null,
    ip_address        varchar(255) null,
    issued_at         timestamp    null,
    value             varchar(255) not null,
    owner_id          int          not null,
    constraint FK71efu05didgncpt1alro69878
        foreign key (owner_id) references users (id)
);

create table notifications
(
    id         int auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    is_read    bit          not null,
    message    varchar(255) null,
    user_id    int          null,
    constraint FK9y21adhxn0ayjhfocscqox7bh
        foreign key (user_id) references users (id)
);

create table persons
(
    id           int auto_increment
        primary key,
    created_at   datetime(6)  null,
    updated_at   datetime(6)  null,
    about_me     varchar(255) null,
    display_name varchar(100) null,
    dob          date         null,
    last_access  date         null,
    location     varchar(255) null,
    website_url  varchar(255) null,
    user_id      int          null,
    constraint UK9ieowpbwhbaefgakkpmkkq4w8
        unique (user_id),
    constraint FKrp309masjisdm7mmqon63obpv
        foreign key (user_id) references users (id)
);

create table questions
(
    id         int auto_increment
        primary key,
    created_at datetime(6)  null,
    updated_at datetime(6)  null,
    body       varchar(255) null,
    closed_at  datetime(6)  null,
    downvotes  int          not null,
    title      varchar(100) null,
    upvotes    int          not null,
    user_id    int          null,
    constraint FKjoo8hp6d3gfwctr68dl2iaemj
        foreign key (user_id) references users (id)
);

create table comments
(
    id          int auto_increment
        primary key,
    created_at  datetime(6)  null,
    updated_at  datetime(6)  null,
    text        varchar(255) null,
    question_id int          null,
    response_to int          null,
    author_id   int          null,
    constraint FK5og2yrimec2mq7km4wgvvybw3
        foreign key (question_id) references questions (id),
    constraint FKn2na60ukhs76ibtpt9burkm27
        foreign key (author_id) references users (id),
    constraint FKqljndgdl4xxe0jwtgud1jtyfq
        foreign key (response_to) references comments (id)
);

create table bookmarks
(
    id          int auto_increment
        primary key,
    comment_id  int null,
    question_id int null,
    user_id     int not null,
    constraint FKdbsho2e05w5r13fkjqfjmge5f
        foreign key (user_id) references users (id),
    constraint FKm0pjqfml3i2teqeadcvqbf8sw
        foreign key (comment_id) references comments (id),
    constraint FKr9msdc6yttbmm1e276c5q9wx2
        foreign key (question_id) references questions (id)
);

create table question_tags
(
    question_id int not null,
    tag_id      int not null,
    primary key (question_id, tag_id),
    constraint FK4s4qdqgvc98lx55s3hu9vqam7
        foreign key (tag_id) references tags (id),
    constraint FKee6kn1hbh2ka2qj64bv30esbw
        foreign key (question_id) references questions (id)
);

create table user_badges
(
    badge_id    int         not null,
    user_id     int         not null,
    achieved_at datetime(6) null,
    primary key (badge_id, user_id),
    constraint FKk6e00pguaij0uke6xr81gt045
        foreign key (badge_id) references badges (id),
    constraint FKr46ah81sjymsn035m4ojstn5s
        foreign key (user_id) references users (id)
);

create table user_oauth
(
    id                int auto_increment
        primary key,
    device_name       varchar(255) null,
    expiration_before datetime(6)  null,
    ip_address        varchar(255) null,
    issued_at         datetime(6)  null,
    oauth_id          varchar(255) null,
    provider          varchar(255) null,
    refresh_token     varchar(255) null,
    owner_id          int          not null,
    constraint UKtktjpl88jem9eccorhnhio6v8
        unique (oauth_id),
    constraint FK9d6bq8cybh6dco03ymtl87ksn
        foreign key (owner_id) references users (id)
);

create table user_roles
(
    user_id int not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id) references roles (id),
    constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id) references users (id)
);

create table vote_types
(
    id   int auto_increment
        primary key,
    type tinyint null,
    check (`type` between 0 and 1)
);

create table votes
(
    id           int auto_increment
        primary key,
    created_at   datetime(6) null,
    updated_at   datetime(6) null,
    comment_id   int         null,
    question_id  int         null,
    user_id      int         not null,
    vote_type_id int         not null,
    constraint FK2hxc17b8uc5l26sij1babwn45
        foreign key (question_id) references questions (id),
    constraint FKec7mutg65es94jvtp4q369r26
        foreign key (vote_type_id) references vote_types (id),
    constraint FKiavg4g6hf3lpw9lyf1uco7h7v
        foreign key (comment_id) references comments (id),
    constraint FKli4uj3ic2vypf5pialchj925e
        foreign key (user_id) references users (id)
);


