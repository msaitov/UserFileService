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

insert into storage_service.user_reg (version, enabled, email, password)
values (0, true, 'vasy@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');
insert into storage_service.user_reg (version, enabled, email, password)
values (0, true, 'dima@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');
insert into storage_service.user_reg (version, enabled, email, password)
values (0, true, 'slava@mail.ru', '$2a$08$RWbutu/bEsuLNBmlTEwViusuO438/FxkEFt1.AAwiQlYt0fwAG/UW');


insert into storage_service.user_access (id, id_user_own, id_user_access, status_access, download_enabled)
values (1, 1, 2, 'ACCESS_ALLOWED_VD', true);
insert into storage_service.user_access (id, id_user_own, id_user_access, status_access, download_enabled)
values (2, 2, 1, 'ACCESS_ALLOWED_VD', true);
insert into storage_service.user_access (id, id_user_own, id_user_access, status_access, download_enabled)
values (3, 2, 3, 'ACCESS_ALLOWED_VD', true);

insert into storage_service.verification_token (user_reg_id, expiry_date, token, version)
values (1, '2019-04-22 16:44:22.361+03', 'abb341e1-602e-4278-9f28-b3386732f2d9', 0);
insert into storage_service.verification_token (user_reg_id, expiry_date, token, version)
values (2, '2018-01-02 16:44:22.361+03', '345fgdfg-sdfs-2222-1111-sdfsdfsdfsdf', 0);


insert into storage_service.downloaded_statistics (user_id, file_name, download_count)
values (1, 'abc.xlsx', 5);
insert into storage_service.downloaded_statistics (user_id, file_name, download_count)
values (3, 'def.xlsx', 10);
