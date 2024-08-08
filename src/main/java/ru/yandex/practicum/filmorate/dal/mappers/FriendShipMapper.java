package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendShip;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Qualifier("friendShipMapper")
public class FriendShipMapper implements RowMapper<FriendShip> {
    @Override
    public FriendShip mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendShip.builder()
                .user1_id(rs.getLong("user1_id"))
                .user2_id(rs.getLong("user2_id"))
                .build();
    }
}