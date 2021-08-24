package com.example.demo.repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

	List<User> findAll();

	List<User> findActiveUsers();

	User findById(int id);

	int insert(User user);

	int update(User user);

	int deleteById(int id);

}
