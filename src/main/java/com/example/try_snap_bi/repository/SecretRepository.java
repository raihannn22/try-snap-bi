package com.example.try_snap_bi.repository;

import com.example.try_snap_bi.model.SecretEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends JpaRepository<SecretEntity, Integer> {
    ;

}
