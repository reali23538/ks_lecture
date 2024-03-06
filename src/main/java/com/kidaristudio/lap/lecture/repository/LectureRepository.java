package com.kidaristudio.lap.lecture.repository;

import com.kidaristudio.lap.lecture.domain.Lecture;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
	
	Page<Lecture> findAll(Pageable pageable);

	/**
	 * 노출할 강연 목록
	 * @param beforeDay
	 * @param afterDay
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Query(value =
			"SELECT T1.* " +
			"  FROM lecture T1 " +
			" WHERE DATE_ADD(T1.lecture_date, INTERVAL :beforeDay DAY) <= NOW() " +
			"    	AND DATE_ADD(T1.lecture_date, INTERVAL :afterDay DAY) >= NOW() " +
			" ORDER BY T1.lecture_date ASC " +
			" LIMIT :offset, :limit ", nativeQuery = true)
	List<Lecture> findAll(
			@Param(value = "beforeDay") int beforeDay,
			@Param(value = "afterDay") int afterDay,
			@Param(value = "offset") int offset,
			@Param(value = "limit") int limit
	);

	/**
	 * 인기 강연 목록
	 * @param beforeDay
	 * @param afterDay
	 * @param standardDay
	 * @param offset
	 * @param limit
	 * @return
	 */
	@Query(value =
			"SELECT T1.*, " +
			"		COUNT(*) AS cnt " +
			"  FROM lecture T1 " +
			"       INNER JOIN apply T2 ON T1.lecture_seq = T2.lecture_seq " +
			" WHERE " +
			"       DATE_ADD(T1.lecture_date, INTERVAL :beforeDay DAY) <= NOW() " +
			"       AND DATE_ADD(T1.lecture_date, INTERVAL :afterDay DAY) >= NOW() " +
			"       AND T2.reg_date >= DATE_ADD(NOW(), INTERVAL :standardDay DAY) " +
			" GROUP BY T2.lecture_seq " +
			"HAVING COUNT(*) > 0 " +
			" ORDER BY cnt DESC " +
			" LIMIT :offset, :limit", nativeQuery = true)
	List<Lecture> findAll(
			@Param(value = "beforeDay") int beforeDay,
			@Param(value = "afterDay") int afterDay,
			@Param(value = "standardDay") int standardDay,
			@Param(value = "offset") int offset,
			@Param(value = "limit") int limit
	);

	/**
	 * 노출될 강연 총개수
	 * @param beforeDay
	 * @param afterDay
	 * @return
	 */
	@Query(value =
			"SELECT COUNT(*) " +
			"  FROM lecture T1 " +
			" WHERE DATE_ADD(T1.lecture_date, INTERVAL :beforeDay DAY) <= NOW() " +
			"    	AND DATE_ADD(T1.lecture_date, INTERVAL :afterDay DAY) >= NOW() ", nativeQuery = true)
	long count(
			@Param(value = "beforeDay") int beforeDay,
			@Param(value = "afterDay") int afterDay
	);

	/**
	 * 인기 강연 총개수
	 * @param beforeDay
	 * @param afterDay
	 * @param standardDay
	 * @return
	 */
	@Query(value =
			"SELECT COUNT(T1.lecture_seq) " +
			"  FROM ( " +
			"			SELECT T1.lecture_seq " +
			"  			  FROM lecture T1 " +
			"       		   INNER JOIN apply T2 ON T1.lecture_seq = T2.lecture_seq " +
			" 			 WHERE " +
			"       		   DATE_ADD(T1.lecture_date, INTERVAL :beforeDay DAY) <= NOW() " +
			"       		   AND DATE_ADD(T1.lecture_date, INTERVAL :afterDay DAY) >= NOW() " +
			"       		   AND T2.reg_date >= DATE_ADD(NOW(), INTERVAL :standardDay DAY) " +
			" 			 GROUP BY T2.lecture_seq " +
			"			HAVING COUNT(*) > 0 " +
			"		) T1", nativeQuery = true)
	long count(
			@Param(value = "beforeDay") int beforeDay,
			@Param(value = "afterDay") int afterDay,
			@Param(value = "standardDay") int standardDay
	);

	/**
	 * 특정 강연 조회 (동시성 문제 해결을 위해 배타적 락 사용)
	 * @param lectureSeq
	 * @return
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select l from Lecture l where l.lectureSeq = :lectureSeq")
	Lecture findByIdWithPessimisticLock(Long lectureSeq);
}
