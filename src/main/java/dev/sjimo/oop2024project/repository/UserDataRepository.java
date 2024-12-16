package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUser_Id(Long User_Id);

    @Query("""
    SELECT ud
    FROM UserData ud
    JOIN User u ON u = ud.user
    WHERE
        u.verified AND (ud.username LIKE CONCAT('%', :searchString, '%') OR u.email LIKE CONCAT(:searchString, '%') OR u.id = CAST(:searchString AS LONG))
    ORDER BY
        CASE
            WHEN u.id = CAST(:searchString AS LONG) THEN 1
            WHEN ud.username LIKE CONCAT('%', :searchString, '%') THEN LENGTH(ud.username)
            WHEN u.email LIKE CONCAT(:searchString, '%') THEN LENGTH(u.email)
            ELSE 1
        END
        ASC
""")
    List<UserData> findByUserDataLike(String searchString);
}
