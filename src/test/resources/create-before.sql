delete
from storage_service.downloaded_statistics;
delete
from storage_service.user_access;
delete
from storage_service.user_role;
delete
from storage_service.verification_token;
delete
from storage_service.user_reg;

ALTER SEQUENCE storage_service.user_reg_id_seq
  RESTART WITH 1;
ALTER SEQUENCE storage_service.user_access_id_seq
  RESTART WITH 1;
ALTER SEQUENCE storage_service.verification_token_id_seq
  RESTART WITH 1;
ALTER SEQUENCE storage_service.downloaded_statistics_id_seq
  RESTART WITH 1;

insert into storage_service.user_reg (enabled, email, password)
values (true, 'vasy@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');
insert into storage_service.user_role (user_id, roles)
values (1, 'USER');

insert into storage_service.user_reg (enabled, email, password)
values (true, 'test@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');
insert into storage_service.user_role (user_id, roles)
values (2, 'ADMIN');

insert into storage_service.user_reg (enabled, email, password)
values (true, 'analist@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');
insert into storage_service.user_role (user_id, roles)
values (3, 'ANALYST');

insert into storage_service.user_access (id_user_own, id_user_access, status_access, download_enabled)
values (2, 1, 'ACCESS_ALLOWED_VD', true);
