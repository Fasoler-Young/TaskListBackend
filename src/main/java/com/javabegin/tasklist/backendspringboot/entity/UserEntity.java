package com.javabegin.tasklist.backendspringboot.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
@Getter
@Entity
@Table(name = "\"user\"")
@Schema(description = "User")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(description = "Identifier of user")
    private Integer id;

    @Column(name = "login", nullable = false, length = 30)
    @Schema(description = "Unique user name", example = "User")
    private String login;

    @Column(name = "password", nullable = false, length = 100)
    @Schema(description = "Secret key")
    private String password;

    @Column(name = "role", nullable = false, length = 20)
    @Schema(description = "User role", example = "ROLE_USER")
    private String role;

    public UserEntity(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }


    // TODO Переделать ролевую модель, это выглядит как костыль
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream((getRole().split(","))).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}