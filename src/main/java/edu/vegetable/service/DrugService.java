package edu.vegetable.service;

import edu.vegetable.model.Drug;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DrugService {

    boolean add(Drug data);

    boolean delete(String drugId);

    boolean update(Drug data);

    Page<Drug> query(Map<String, String> condition);

    long queryTotal(Map<String, String> condition);

    Drug queryById(String drugId);

}
