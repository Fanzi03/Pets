package org.example.mapping.util;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;

public interface MapperServiceHelper {
    
    default <T,V> List<V> mapList(List<T> list, Function<T,V> mapper){
        if(list == null || mapper == null) return Collections.emptyList();
        return (List<V>) list.stream().map(mapper).toList();
    }

    default <T,V> V map(T object, Function<T,V> mapper){
        if(object == null || mapper == null) return null;
        return (V) mapper.apply(object);
    }

    default <T,V> Page<V> mapPage(Page<T> page, Function<T,V> mapper){
        if(page == null || mapper == null) return Page.empty();
        return page.map(mapper);
    }
}
