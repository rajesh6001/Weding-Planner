package com.weddingplanning.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingplanning.entity.Category;
import com.weddingplanning.entity.Event;
import com.weddingplanning.entity.User;

@Repository
public interface EventDao extends JpaRepository<Event, Integer> {

	List<Event> findByStatusOrderByIdDesc(String status);

	List<Event> findByStatusAndNameContainingIgnoreCase(String status, String name);

	List<Event> findByStatusAndCategory(String status, Category category);

	List<Event> findByManager(User manager);

}
