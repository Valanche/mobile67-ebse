package nju.mobile67.service;

import nju.mobile67.model.entity.Type;

import java.util.List;
import java.util.Optional;

public interface TypeService {
    Type saveType(Type type);
    Optional<Type> getTypeById(Long id);
    List<Type> getAllTypes();
    void deleteType(Long id);
}
