package nju.mobile67.repository;

import nju.mobile67.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String typeName);
}
