package nju.mobile67.repository;

import nju.mobile67.model.entity.Logs;
import nju.mobile67.model.entity.Type;
import nju.mobile67.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Logs, Long> {
    List<Logs> findAllByUserAndType(User user, Type type);
}
