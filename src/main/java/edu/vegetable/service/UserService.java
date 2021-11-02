package edu.vegetable.service;

import edu.vegetable.model.User;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface UserService {

    boolean add(User data);

    boolean delete(String id);

    boolean update(User user);

    Page<User> query(Map<String, String> condition);

    long queryTotal(Map<String, String> condition);

    User queryById(String userid);

    User login(String userName, String password);

    boolean isUnique(String userName);
}
