package com.bastiansmn.vp.shared;

import java.util.Collection;
import java.util.Optional;

public interface CrudService<T, U> {

    T create(T t);

    Optional<T> fetchById(U id);

    T update(T t);

    void delete(U id);

    Collection<T> fetchAll();

}
