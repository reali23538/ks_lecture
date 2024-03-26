package com.ks.lap.apply;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.apply.domain.ApplyDto;
import com.ks.lap.apply.repository.ApplyRepository;
import com.ks.lap.apply.service.ApplyService;
import com.ks.lap.common.constant.lecture.LectureHallCode;
import com.ks.lap.common.exception.AppliedLectureException;
import com.ks.lap.common.exception.NotAppliedLectureException;
import com.ks.lap.common.exception.SoldOutLectureException;
import com.ks.lap.lecture.domain.Lecture;
import com.ks.lap.lecture.service.LectureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplyServiceTest {

    @Mock
    private LectureService lectureService;

    @InjectMocks
    private ApplyService applyService;

    @Mock
    private ApplyRepository applyRepository;

    @DisplayName("특정 강연의 신청자 목록 조회 테스트")
    @Test
    void getApplicants() {
        // given
        Long lectureSeq = 1L;
        ApplyDto.GetApplicantsReq req = ApplyDto.GetApplicantsReq.builder()
                .currentPage(1)
                .lecture(new ApplyDto.GetApplicantsReq.Lecture(lectureSeq))
                .build();
        doReturn(applyList())
                .when(applyRepository)
                .findAllByLecture(any(Lecture.class), any(PageRequest.class));

        // when
        ApplyDto.GetApplicantsRes applicantsRes = applyService.getApplicants(req);
        System.out.println("size >>> " + applicantsRes.getApplicants().size());

        // then
        assertThat(applicantsRes.getApplicants().size()).isEqualTo(5);
    }

    @DisplayName("강연 신청 테스트")
    @Test
    void apply() throws AppliedLectureException, SoldOutLectureException {
        // given
        Lecture lecture = lecture(1L);
        String employeeNumber = "11111";
        int appliedCnt = 0;
        ApplyDto.ApplyReq req = ApplyDto.ApplyReq.builder()
                .lecture(new ApplyDto.ApplyReq.Lecture(lecture.getLectureSeq()))
                .employeeNumber(employeeNumber)
                .build();
        doReturn(lecture)
                .when(lectureService)
                .getLecture(anyLong());
        doReturn(appliedCnt)
                .when(applyRepository)
                .countByLectureAndEmployeeNumber(any(Lecture.class), anyString());
        Mockito.doReturn(apply(1L))
                .when(applyRepository)
                .save(any(Apply.class));

        // when
        ApplyDto.ApplyRes res = applyService.apply(req);
        System.out.println("res applySeq >>> " + res.getApplySeq());

        // then
        assertThat(res.getApplySeq()).isEqualTo(1L);

        // verify
        verify(applyRepository, times(1)).save(any(Apply.class));
    }

    @DisplayName("강연 취소 테스트")
    @Test
    void cancel() throws NotAppliedLectureException {
        // given
        Lecture lecture = lecture(1L);
        String employeeNumber = "11111";
        ApplyDto.ApplyReq req = ApplyDto.ApplyReq.builder()
                .lecture(new ApplyDto.ApplyReq.Lecture(lecture.getLectureSeq()))
                .employeeNumber(employeeNumber)
                .build();
        doReturn(lecture)
                .when(lectureService)
                .getLecture(anyLong());
        doReturn(Optional.of(apply(1L)))
                .when(applyRepository)
                .findByLectureAndEmployeeNumber(any(Lecture.class), anyString());
        doNothing()
                .when(applyRepository)
                .delete(any(Apply.class));

        // when
        applyService.cancel(req);

        // verify
        verify(applyRepository, times(1)).delete(any(Apply.class));
    }

    private Page<Apply> applyList() {
        List<Apply> applies = new ArrayList<>();
        for (long i=1; i<=5; i++) {
            Apply apply = apply(i);
            applies.add(apply);
        }
        return new PageImpl<>(applies);
    }

    private Apply apply(Long i) {
        Apply apply = new Apply();
        apply.setApplySeq(i);
        apply.setLecture(lecture(1L));
        apply.setEmployeeNumber("11111");
        apply.setRegDate(new Date());
        return apply;
    }

    private Lecture lecture(Long i) {
        Lecture lecture = new Lecture();
        lecture.setLectureSeq(i);
        lecture.setLecturer("강연자" + i);
        lecture.setLectureHallCode(LectureHallCode.KS001);
        lecture.setLectureDate(new Date());
        lecture.setLectureContent("강연자" + i + "의 특별강연");
        lecture.setApplyCnt(5);
        lecture.setMaxCnt(100);
        lecture.setRegDate(new Date());
        return lecture;
    }

}
