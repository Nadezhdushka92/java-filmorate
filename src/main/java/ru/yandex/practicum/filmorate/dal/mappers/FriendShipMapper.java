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
                .user1Id(rs.getLong("user1Id"))
                .user2Id(rs.getLong("user2Id"))
                .build();
    }
}