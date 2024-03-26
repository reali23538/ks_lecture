package com.ks.lap.lecture;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.apply.repository.ApplyRepository;
import com.ks.lap.common.constant.lecture.LectureHallCode;
import com.ks.lap.lecture.domain.Lecture;
import com.ks.lap.lecture.repository.LectureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplyRepository applyRepository;

    @DisplayName("강연 등록 테스트")
    @Test
    void save() {
        // given
        Lecture lecture = lecture();

        // when
        Lecture savedLecture = lectureRepository.save(lecture);
        System.out.println("savedLecture lectureSeq >>> " + savedLecture.getLectureSeq());

        // then
        assertThat(savedLecture.getLecturer()).isEqualTo(lecture.getLecturer());
        assertThat(savedLecture.getLectureHallCode()).isEqualTo(lecture.getLectureHallCode());
    }

    @DisplayName("강연 목록 조회 테스트")
    @Test
    void findAll() {
        // given
        lectureRepository.save(lecture());
        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<Lecture> pLectures = lectureRepository.findAll(pageable);
        System.out.println("size >>> " + pLectures.getContent().size());

        // then
        assertThat(pLectures.getContent().size()).isEqualTo(1);
    }

    @DisplayName("노출할 강연 목록 조회 테스트")
    @Test
    void findAllForExposed() {
        // given
        lectureRepository.save(lecture());

        // when
        List<Lecture> lectures = lectureRepository.findAll(-7, 1, 0, 5);
        System.out.println("size >>> " + lectures.size());

        // then
        assertThat(lectures.size()).isEqualTo(1);
    }

    @DisplayName("인기 강연 목록 조회 테스트")
    @Test
    void findAllForPopular() {
        // given
        Lecture lecture = lectureRepository.save(lecture());
        applyRepository.save(apply(lecture));

        // when
        List<Lecture> lectures = lectureRepository.findAll(-7, 1, -3, 0, 5);
        System.out.println("size >>> " + lectures.size());

        // then
        assertThat(lectures.size()).isEqualTo(1);
    }

    @DisplayName("노출할 강연 총개수 조회 테스트")
    @Test
    void countForExposed() {
        // given
        lectureRepository.save(lecture());

        // when
        long exposedCnt = lectureRepository.count(-7, 1);
        System.out.println("exposedCnt >>> " + exposedCnt);

        // then
        assertThat(exposedCnt).isEqualTo(1);
    }

    @DisplayName("인기 강연 총개수 조회 테스트")
    @Test
    void countForPopular() {
        // given
        Lecture lecture = lectureRepository.save(lecture());
        applyRepository.save(apply(lecture));

        // when
        long popularCnt = lectureRepository.count(-7, 1, -3);
        System.out.println("popularCnt >>> " + popularCnt);

        // then
        assertThat(popularCnt).isEqualTo(1);
    }

    @DisplayName("강연 조회 테스트")
    @Test
    void findByIdWithPessimisticLock() {
        // given
        Lecture savedLecture = lectureRepository.save(lecture());
        System.out.println("savedLecture lectureSeq >>> " + savedLecture.getLectureSeq());

        // when
        Lecture lecture = lectureRepository.findByIdWithPessimisticLock(savedLecture.getLectureSeq());

        // then
        assertThat(lecture.getLecturer()).isEqualTo("김강연");
    }

    @DisplayName("강연 조회 테스트")
    @Test
    void findById() {
        // given
        Lecture savedLecture = lectureRepository.save(lecture());
        System.out.println("savedLecture lectureSeq >>> " + savedLecture.getLectureSeq());

        // when
        Optional<Lecture> oLecture = lectureRepository.findById(savedLecture.getLectureSeq());

        // then
        assertThat(oLecture.isPresent()).isEqualTo(true);
        assertThat(oLecture.get().getLecturer()).isEqualTo("김강연");
    }

    private Lecture lecture() {
        Lecture lecture = new Lecture();
        lecture.setLecturer("김강연");
        lecture.setLectureHallCode(LectureHallCode.KS001);
        lecture.setLectureDate(new Date());
        lecture.setLectureContent("김강연의 특별강연");
        lecture.setApplyCnt(0);
        lecture.setMaxCnt(100);
        lecture.setRegDate(new Date());
        return lecture;
    }

    private Apply apply(Lecture lecture) {
        Apply apply = new Apply();
        apply.setLecture(lecture);
        apply.setRegDate(new Date());
        apply.setEmployeeNumber("11111");
        return apply;
    }

}
