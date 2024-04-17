package nju.mobile67.service.impl;

import nju.mobile67.model.entity.Type;
import nju.mobile67.repository.TypeRepository;
import nju.mobile67.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    TypeRepository typeRepository;


    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Optional<Type> getTypeById(Long id) {
        return typeRepository.findById(id);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
