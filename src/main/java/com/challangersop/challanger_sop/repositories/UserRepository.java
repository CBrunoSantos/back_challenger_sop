package com.challangersop.challanger_sop.repositories;

import com.challangersop.challanger_sop.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
