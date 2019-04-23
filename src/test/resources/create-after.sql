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