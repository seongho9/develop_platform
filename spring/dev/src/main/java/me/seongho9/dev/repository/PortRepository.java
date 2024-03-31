package me.seongho9.dev.repository;

import me.seongho9.dev.domain.ExposePorts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortRepository extends JpaRepository<ExposePorts, Long> {


    @Query(
            nativeQuery = true,
            value = "SELECT MAX(end) from exposed_port"
    )
    Optional<Integer> findEndOfPorts();
}
