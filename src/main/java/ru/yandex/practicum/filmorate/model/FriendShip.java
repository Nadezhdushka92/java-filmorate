package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendShip {
    private Long user1_id;
    private Long user2_id;
    private int statusFriend;

    public FriendShip(Long user1Id, Long user2Id) {
        this.user1_id = user1Id;
        this.user2_id = user2Id;
        this.statusFriend = 1;
    }
}
