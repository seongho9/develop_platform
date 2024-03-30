package me.seongho9.dev.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name="exposed_port")
public class ExposePorts {

    @Id
    @Column(name="port_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start", unique = true, nullable = false)
    @NotNull
    private Integer start;
    @Column(name = "current", unique = true, nullable = false)
    private Integer current;
    @NotNull
    @Column(name = "end", unique = true, nullable = false)
    private Integer end;

}
