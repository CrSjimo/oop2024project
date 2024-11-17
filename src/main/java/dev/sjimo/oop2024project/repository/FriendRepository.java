package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.Friend;
import dev.sjimo.oop2024project.model.User;
import dev.sjimo.oop2024project.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Friend u " +
            "WHERE (u.user1 = :user1 AND u.user2 = :user2) OR (u.user1 = :user2 AND u.user2 = :user1)")
    boolean existsByUserAndUser2(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT u " +
            "FROM Friend u " +
            "WHERE (u.user1 = :user1 AND u.user2 = :user2) OR (u.user1 = :user2 AND u.user2 = :user1)")
    Optional<Friend> findByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);
}
