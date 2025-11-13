package com.example.try_snap_bi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "secret_key")
@Data
public class SecretEntity {
    @Id
    private Integer id;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column (name = "client_key")
    private String clientKey;

    @Column (name = "public_key",length = 500)
    private String publicKey;
}
