package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ClockIn;

import java.util.ArrayList;

@Repository
public interface ClockInRepo extends JpaRepository<ClockIn, Long> {
	
	ArrayList<ClockIn> findAll();

    @Query(value = "SELECT * FROM clock_in WHERE staff_id = ?1 and end_time IS NULL", nativeQuery = true)
	ArrayList<ClockIn> findByNotClockOutRecord(Long staffId);

    @Query(value = "SELECT * FROM clock_in WHERE staff_id = ?1 and YEAR(start_time) = ?2 and MONTH(start_time) = ?3 ORDER BY start_time ASC", nativeQuery = true)
	ArrayList<ClockIn> getClockInList(Long staffId, Integer year, Integer month);

	// @Query(value = "select name,author,price from Book b where b.price>?1 and b.price<?2")
	// List<Book> findByPriceRange(long price1, long price2);
}