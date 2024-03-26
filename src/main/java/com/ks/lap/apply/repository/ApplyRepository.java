package com.ks.lap.apply.repository;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.lecture.domain.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
	
	Page<Apply> findAllByLecture(Lecture lecture, Pageable pageable);

	int countByLectureAndEmployeeNumber(Lecture lecture, String employeeNumber);

	List<Apply> findAllByEmployeeNumber(String employeeNumber);

	Optional<Apply> findByLectureAndEmployeeNumber(Lecture lecture, String employeeNumber);

}
