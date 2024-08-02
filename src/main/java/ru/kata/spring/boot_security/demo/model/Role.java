package ru.kata.spring.boot_security.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles_table")
@Setter
@Getter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_id")
    private Integer id;

    private String type;

    public Role() {
    }

    @Override
    public String getAuthority() {
        return getType();
    }
}
