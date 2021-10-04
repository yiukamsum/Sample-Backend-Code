package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	ArrayList<User> findAll();

	Optional<User> findById(Long id);

	ArrayList<User> findByEmail(String email);

	ArrayList<User> findByObjectguid(String objectguid);

	// @Query(value = "select name,author,price from Book b where b.price>?1 and b.price<?2")
	// List<Book> findByPriceRange(long price1, long price2);
}
