package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Friend;
import dev.sjimo.oop2024project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " + "FROM Friend u " + "WHERE (u.user1 = :user1 AND u.user2 = :user2) OR (u.user1 = :user2 AND u.user2 = :user1)")
    boolean existsByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT u " + "FROM Friend u " + "WHERE (u.user1 = :user1 AND u.user2 = :user2) OR (u.user1 = :user2 AND u.user2 = :user1)")
    Optional<Friend> findByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);

    @Query(value = """
            SELECT
                f.*
            FROM
                friend f
            JOIN
                user u ON u.id = CASE WHEN f.user1_id = :userId THEN f.user2_id ELSE f.user1_id END
            JOIN
                user_data ud ON u.id = ud.user_id
            WHERE
                f.user1_id = :userId OR f.user2_id = :userId
            ORDER BY
                CASE WHEN CASE WHEN f.user1_id = :userId THEN f.comment_name2 ELSE f.comment_name1 END IS NOT NULL THEN CASE WHEN f.user1_id = :userId THEN f.comment_name2 ELSE f.comment_name1 END ELSE ud.username END
            COLLATE
                utf8mb4_zh_0900_as_cs
            """, nativeQuery = true)
    List<Friend> listFriendsWithCollation(@Param("userId") Long userId);
}
