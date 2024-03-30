package me.seongho9.dev.repository;

import me.seongho9.dev.domain.container.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContainerRepository extends JpaRepository<Container, String> {

    @Query( nativeQuery = true,
            value = "SELECT id from container where user_id=:user AND name=:container"
    )
    String findContainerId(
            @Param("container") String containerName,
            @Param("user") String userId
            );

    @Query(nativeQuery = true,
            value = "SELECT * FROM container WHERE user_id=:user")
    Optional<List<Container>> findContainerListByUserId(@Param("user") String userId);
}
