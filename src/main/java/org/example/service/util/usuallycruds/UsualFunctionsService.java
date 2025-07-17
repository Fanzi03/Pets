package org.example.service.util.usuallycruds;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsualFunctionsService<T>{
    public Page<T> gets(Pageable page);
    public T findById(Long id);
}
