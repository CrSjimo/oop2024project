package dev.sjimo.oop2024project.repository;

import dev.sjimo.oop2024project.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByUser_Id(Long User_Id);
}
