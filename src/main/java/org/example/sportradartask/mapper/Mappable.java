package org.example.sportradartask.mapper;


import org.mapstruct.MappingTarget;

import java.util.List;

public interface Mappable<E, D> {

    D toDto(E entity);

    List<D> toDto(List<E> entities);

    E toEntity(D dto);

    void updateEntityFromDto(D dto, @MappingTarget E entity);


}
