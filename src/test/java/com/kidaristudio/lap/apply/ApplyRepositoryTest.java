package com.kidaristudio.lap.apply;

import com.kidaristudio.lap.apply.domain.Apply;
import com.kidaristudio.lap.apply.repository.ApplyRepository;
import com.kidaristudio.lap.common.constant.lecture.LectureHallCode;
import com.kidaristudio.lap.lecture.domain.Lecture;
import com.kidaristudio.lap.lecture.repository.LectureRepository;
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
public class ApplyRepositoryTest {

    @Autowired
    private ApplyRepository applyRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @DisplayName("신청 등록 테스트")
    @Test
    void save() {
        // given
        Apply apply = apply();

        // when
        Apply savedApply = applyRepository.save(apply);
        System.out.println("savedApply applySeq >>> " + savedApply.getApplySeq());

        // then
        assertThat(savedApply.getEmployeeNumber()).isEqualTo(apply.getEmployeeNumber());
    }

    @DisplayName("특정 강연의 신청 목록 조회 테스트")
    @Test
    void findAllByLecture() {
        // given
        Apply savedApply = applyRepository.save(apply());
        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<Apply> pApplies  = applyRepository.findAllByLecture(savedApply.getLecture(), pageable);
        System.out.println("size >>> " + pApplies.getContent().size());

        // then
        assertThat(pApplies.getContent().size()).isEqualTo(1);
    }

    @DisplayName("특정 직원이 특정 강연을 신청한 횟수 조회 테스트")
    @Test
    void countByLectureAndEmployeeNumber() {
        // given
        Apply savedApply = applyRepository.save(apply());

        // when
        int applyCnt  = applyRepository.countByLectureAndEmployeeNumber(savedApply.getLecture(), savedApply.getEmployeeNumber());
        System.out.println("applyCnt >>> " + applyCnt);

        // then
        assertThat(applyCnt).isEqualTo(1);
    }

    @DisplayName("특정 직원의 신청 목록 조회 테스트")
    @Test
    void findAllByEmployeeNumber() {
        // given
        Apply savedApply = applyRepository.save(apply());

        // when
        List<Apply> applies = applyRepository.findAllByEmployeeNumber(savedApply.getEmployeeNumber());
        System.out.println("size >>> " + applies.size());

        // then
        assertThat(applies.size()).isEqualTo(1);
    }

    @DisplayName("특정 직원이 신청한 특정 강연의 신청 상세 조회 테스트")
    @Test
    void findByLectureAndEmployeeNumber() {
        // given
        Apply savedApply = applyRepository.save(apply());
        System.out.println("savedApply applySeq >>> " + savedApply.getApplySeq());

        // when
        Optional<Apply> oApply = applyRepository.findByLectureAndEmployeeNumber(savedApply.getLecture(), savedApply.getEmployeeNumber());

        // then
        assertThat(oApply.isPresent()).isEqualTo(true);
        assertThat(oApply.get().getEmployeeNumber()).isEqualTo("11111");
    }

    @DisplayName("신청 삭제 테스트")
    @Test
    void delete() {
        // given
        Apply savedApply = applyRepository.save(apply());
        System.out.println("savedApply applySeq >>> " + savedApply.getApplySeq());

        // when
        applyRepository.delete(savedApply);
        System.out.println("deleted!!");

        // then
        Optional<Apply> oApply = applyRepository.findById(savedApply.getApplySeq());
        System.out.println("isPresent >>> " + oApply.isPresent());
        assertThat(oApply.isPresent()).isEqualTo(false);
    }

    private Apply apply() {
        Apply apply = new Apply();
        apply.setLecture(lecture());
        apply.setEmployeeNumber("11111");
        apply.setRegDate(new Date());
        return apply;
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
        return lectureRepository.save(lecture);
    }

}
