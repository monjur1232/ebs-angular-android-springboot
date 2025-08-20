package com.erp.common;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface ICommonController<T> {

	public T save(T t);

	public ResponseEntity<T> getById(Long id);

	public ResponseEntity<T> update(Long id, T t);

	public Map<String, Boolean> delete(Long id);

	public List<T> getAll();
	
}
