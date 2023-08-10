package com.poly.dao;

import java.util.List;
import java.util.Map;

import com.poly.dto.UserDto;
import com.poly.entity.User;

public interface UserDao {

	User findById(Integer id);

	User findByEmail(String email);

	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

	List<User> findAll();

	List<User> findAll(int pageNumber, int pageSize);

	User create(User entity);

	User update(User entity);

	User detele(User entity);

	List<User> findUsersLikedVideoByVideoHref1(Map<String, Object> params);
}
