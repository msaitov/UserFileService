package ru.msaitov.model.mapper;

import ru.msaitov.model.UserEntity;
import ru.msaitov.view.UserView;

/**
 * Маппер из Entity в View и обратно
 */
public interface Mapper {

    UserEntity map(UserView userView);

    UserView map(UserEntity userEntity);
}
