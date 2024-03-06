package com.kidaristudio.lap.lecture;

import com.kidaristudio.lap.apply.domain.Apply;
import com.kidaristudio.lap.apply.repository.ApplyRepository;
import com.kidaristudio.lap.common.constant.lecture.LectureHallCode;
import com.kidaristudio.lap.common.exception.NoApplyLectureException;
import com.kidaristudio.lap.common.util.DateUtil;
import com.kidaristudio.lap.lecture.domain.Lecture;
import com.kidaristudio.lap.lecture.domain.LectureDto;
import com.kidaristudio.lap.lecture.repository.LectureRepository;
import com.kidaristudio.lap.lecture.service.LectureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private ApplyRepository applyRepository;

    @DisplayName("강연 목록 조회 테스트")
    @Test
    void getLectures() {
        // given
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(1);
        doReturn(pLectureList())
                .when(lectureRepository)
                .findAll(any(PageRequest.class));

        // when
        LectureDto.GetLecturesRes lecturesRes = lectureService.getLectures(req);
        System.out.println("size >>> " + lecturesRes.getLectures().size());

        // then
        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
    }

    @DisplayName("노출할 강연 목록 조회 테스트")
    @Test
    void getExposedLectures() {
        // given
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(1);
        doReturn(lectureList())
                .when(lectureRepository)
                .findAll(anyInt(), anyInt(), anyInt(), anyInt());
        doReturn(5L)
                .when(lectureRepository)
                .count(anyInt(), anyInt());

        // when
        LectureDto.GetLecturesRes lecturesRes = lectureService.getExposedLectures(req);
        System.out.println("size >>> " + lecturesRes.getLectures().size());

        // then
        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
    }

    @DisplayName("인기 강연 목록 조회 테스트")
    @Test
    void getPopularLectures() {
        // given
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(1);
        doReturn(lectureList())
                .when(lectureRepository)
                .findAll(anyInt(), anyInt(), anyInt(), anyInt(), anyInt());
        doReturn(5L)
                .when(lectureRepository)
                .count(anyInt(), anyInt(), anyInt());

        // when
        LectureDto.GetLecturesRes lecturesRes = lectureService.getPopularLectures(req);
        System.out.println("size >>> " + lecturesRes.getLectures().size());

        // then
        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
    }

    @DisplayName("특정 직원이 신청한 강연 목록 조회 테스트")
    @Test
    void getLecturesByEmployeeNumber() throws NoApplyLectureException {
        // given
        String employeeNumber = "11111";
        doReturn(applyList())
                .when(applyRepository)
                .findAllByEmployeeNumber(anyString());

        // when
        LectureDto.GetLecturesByEmployeeNumberRes lecturesRes = lectureService.getLectures(employeeNumber);
        System.out.println("size >>> " + lecturesRes.getLectures().size());

        // then
        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
    }

    @DisplayName("강연 상세 조회 테스트")
    @Test
    void getLecture() {
        // given
        Lecture lecture = lecture(1L);
        doReturn(lecture)
                .when(lectureRepository)
                .findByIdWithPessimisticLock(anyLong());

        // when
        Lecture resultLecture = lectureService.getLecture(lecture.getLectureSeq());
        System.out.println("resultLecture lectureSeq >>> " + resultLecture.getLectureSeq());

        // then
        assertThat(resultLecture.getLectureSeq()).isEqualTo(lecture.getLectureSeq());
    }

    @DisplayName("강연 등록 테스트")
    @Test
    void add() throws ParseException {
        // given
        Lecture lecture = lecture(1L);
        LectureDto.AddReq req = LectureDto.AddReq.builder()
                .lecturer(lecture.getLecturer())
                .lectureHallCode(lecture.getLectureHallCode())
                .lectureDate(DateUtil.dateToString(lecture.getLectureDate()))
                .lectureContent(lecture.getLectureContent())
                .maxCnt(lecture.getMaxCnt())
                .build();
        doReturn(lecture)
                .when(lectureRepository)
                .save(any(Lecture.class));

        // when
        LectureDto.AddRes res = lectureService.add(req);
        System.out.println("res lecturer >>> " + res.getLecturer());

        // then
        assertThat(res.getLecturer()).isEqualTo(req.getLecturer());
        assertThat(res.getLectureHallCode()).isEqualTo(req.getLectureHallCode());

        // verify
        verify(lectureRepository, times(1)).save(any(Lecture.class));
    }

    private Page<Lecture> pLectureList() {
        return new PageImpl<>(lectureList());
    }

    private List<Lecture> lectureList() {
        List<Lecture> lectures = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            Lecture lecture = lecture(i);
            lectures.add(lecture);
        }
        return lectures;
    }

    private Lecture lecture(Long i) {
        Lecture lecture = new Lecture();
        lecture.setLectureSeq(i);
        lecture.setLecturer("강연자" + i);
        lecture.setLectureHallCode(LectureHallCode.KS001);
        lecture.setLectureDate(new Date());
        lecture.setLectureContent("강연자" + i + "의 특별강연");
        lecture.setApplyCnt(1);
        lecture.setMaxCnt(100);
        lecture.setRegDate(new Date());
        return lecture;
    }

    private List<Apply> applyList() {
        List<Apply> applies = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            Apply apply = apply(i);
            applies.add(apply);
        }
        return applies;
    }

    private Apply apply(Long i) {
        Apply apply = new Apply();
        apply.setApplySeq(i);
        apply.setLecture(lecture(i));
        apply.setEmployeeNumber("11111");
        apply.setRegDate(new Date());
        return apply;
    }

}
