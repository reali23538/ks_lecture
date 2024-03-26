package com.ks.lap.apply.service;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.apply.domain.ApplyDto;
import com.ks.lap.apply.repository.ApplyRepository;
import com.ks.lap.common.exception.AppliedLectureException;
import com.ks.lap.common.exception.NotAppliedLectureException;
import com.ks.lap.common.exception.SoldOutLectureException;
import com.ks.lap.common.util.DateUtil;
import com.ks.lap.lecture.domain.Lecture;
import com.ks.lap.lecture.repository.LectureRepository;
import com.ks.lap.lecture.service.LectureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplyService {

    private final ApplyRepository applyRepository;

    private final LectureRepository lectureRepository;

    private final LectureService lectureService;

    @Value("${constant.common.paging.row-cnt-per-page}")
    private int ROW_CNT_PER_PAGE;

    public ApplyDto.GetApplicantsRes getApplicants(ApplyDto.GetApplicantsReq req) {
        PageRequest pageRequest = PageRequest.of(req.getCurrentPage() - 1, ROW_CNT_PER_PAGE, Sort.Direction.DESC, "applySeq");
        Page<Apply> pApplicants = applyRepository.findAllByLecture(new Lecture(req.getLecture().getLectureSeq()), pageRequest);

        List<ApplyDto.GetApplicantsRes.Applicant> applicants = pApplicants.map(applicant ->
                ApplyDto.GetApplicantsRes.Applicant.builder()
                        .applySeq(applicant.getApplySeq())
                        .employeeNumber(applicant.getEmployeeNumber())
                        .regDate(DateUtil.dateToString(applicant.getRegDate()))
                        .build()
        ).getContent();
        return ApplyDto.GetApplicantsRes.builder()
                .applicants(applicants)
                .totalCount(pApplicants.getTotalElements())
                .rowCntPerPage(ROW_CNT_PER_PAGE)
                .build();
    }

    @Transactional
    public ApplyDto.ApplyRes apply(ApplyDto.ApplyReq req) throws AppliedLectureException, SoldOutLectureException {
        Lecture lecture = new Lecture(req.getLecture().getLectureSeq());

        // 중복 신청 체크
        int appliedCnt = applyRepository.countByLectureAndEmployeeNumber(lecture, req.getEmployeeNumber());
        if (appliedCnt > 0) throw new AppliedLectureException("이미 신청한 강연입니다.");

        // 신청 횟수 증가
        increaseApplyCnt(req.getLecture().getLectureSeq());

        Apply apply = new Apply();
        apply.setLecture(lecture);
        apply.setEmployeeNumber(req.getEmployeeNumber());
        apply.setRegDate(new Date());
        Apply savedApply = applyRepository.save(apply);
        return ApplyDto.ApplyRes.builder()
                .applySeq(savedApply.getApplySeq())
                .regDate(DateUtil.dateToString(savedApply.getRegDate()))
                .build();
    }

    /**
     * 신청 횟수 증가. 매진 체크
     * @param lectureSeq
     * @throws Exception
     */
    @Transactional
    public void increaseApplyCnt(Long lectureSeq) throws SoldOutLectureException {
        Lecture lecture = lectureService.getLecture(lectureSeq);

        if (lecture.getApplyCnt() >= lecture.getMaxCnt()) throw new SoldOutLectureException("매진된 강연입니다.");
        lecture.setApplyCnt(lecture.getApplyCnt() + 1);
        lectureRepository.saveAndFlush(lecture);
    }

    @Transactional
    public void cancel(ApplyDto.ApplyReq req) throws NotAppliedLectureException {
        Lecture lecture = new Lecture(req.getLecture().getLectureSeq());

        Optional<Apply> oApply = applyRepository.findByLectureAndEmployeeNumber(lecture, req.getEmployeeNumber());
        if (!oApply.isPresent()) throw new NotAppliedLectureException("신청하지않은 강연입니다.");

        // 신청 횟수 감소
        decreaseApplyCnt(lecture.getLectureSeq());
        applyRepository.delete(oApply.get());
    }

    /**
     * 신청 횟수 감소
     * @param lectureSeq
     * @throws Exception
     */
    @Transactional
    public void decreaseApplyCnt(Long lectureSeq) {
        Lecture lecture = lectureService.getLecture(lectureSeq);

        if (lecture.getApplyCnt() > 0) {
            lecture.setApplyCnt(lecture.getApplyCnt() - 1);
            lectureRepository.saveAndFlush(lecture);
        }
    }

}
