package com.ks.lap.lecture.service;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.apply.repository.ApplyRepository;
import com.ks.lap.common.exception.NoApplyLectureException;
import com.ks.lap.common.util.DateUtil;
import com.ks.lap.lecture.domain.Lecture;
import com.ks.lap.lecture.domain.LectureDto;
import com.ks.lap.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    private final ApplyRepository applyRepository;

    @Value("${constant.common.paging.row-cnt-per-page}")
    private int ROW_CNT_PER_PAGE;

    @Value("${constant.lecture.exposed.before-day}")
    private int BEFORE_DAY;

    @Value("${constant.lecture.exposed.after-day}")
    private int AFTER_DAY;

    @Value("${constant.lecture.popular.standard-day}")
    private int STANDARD_DAY;

    public LectureDto.GetLecturesRes getLectures(LectureDto.GetLecturesReq req) {
        PageRequest pageRequest = PageRequest.of(req.getCurrentPage() - 1, ROW_CNT_PER_PAGE, Sort.Direction.DESC, "lectureSeq");
        Page<Lecture> pLectures = lectureRepository.findAll(pageRequest);

        List<LectureDto.GetLecturesRes.Lecture> lectures = pLectures.map(
                lecture -> new LectureDto.GetLecturesRes.Lecture(lecture)
        ).getContent();
        return LectureDto.GetLecturesRes.builder()
                .lectures(lectures)
                .totalCount(pLectures.getTotalElements())
                .rowCntPerPage(ROW_CNT_PER_PAGE)
                .build();
    }

    public LectureDto.GetLecturesRes getExposedLectures(LectureDto.GetLecturesReq req) {
        List<Lecture> lectures = lectureRepository.findAll(BEFORE_DAY, AFTER_DAY, req.getCurrentPage() - 1, ROW_CNT_PER_PAGE);
        long totalCount = lectureRepository.count(BEFORE_DAY, AFTER_DAY);

        List<LectureDto.GetLecturesRes.Lecture> resultLectures = lectures.stream().map(
                lecture -> new LectureDto.GetLecturesRes.Lecture(lecture)
        ).collect(Collectors.toList());
        return LectureDto.GetLecturesRes.builder()
                .lectures(resultLectures)
                .totalCount(totalCount)
                .rowCntPerPage(ROW_CNT_PER_PAGE)
                .build();
    }

    public LectureDto.GetLecturesRes getPopularLectures(LectureDto.GetLecturesReq req) {
        List<Lecture> lectures = lectureRepository.findAll(BEFORE_DAY, AFTER_DAY, STANDARD_DAY, req.getCurrentPage() - 1, ROW_CNT_PER_PAGE);
        long totalCount = lectureRepository.count(BEFORE_DAY, AFTER_DAY, STANDARD_DAY);

        List<LectureDto.GetLecturesRes.Lecture> resultLectures = lectures.stream().map(
                lecture -> new LectureDto.GetLecturesRes.Lecture(lecture)
        ).collect(Collectors.toList());
        return LectureDto.GetLecturesRes.builder()
                .lectures(resultLectures)
                .totalCount(totalCount)
                .rowCntPerPage(ROW_CNT_PER_PAGE)
                .build();
    }

    public LectureDto.GetLecturesByEmployeeNumberRes getLectures(String employeeNumber) throws NoApplyLectureException {
        List<Apply> applies = applyRepository.findAllByEmployeeNumber(employeeNumber);
        if (CollectionUtils.isEmpty(applies)) throw new NoApplyLectureException("신청한 강연이 없습니다.");

        List<LectureDto.GetLecturesByEmployeeNumberRes.Lecture> lectures = applies.stream().map(apply -> {
            Lecture lecture = apply.getLecture();

            return LectureDto.GetLecturesByEmployeeNumberRes.Lecture.builder()
                    .lectureSeq(lecture.getLectureSeq())
                    .lecturer(lecture.getLecturer())
                    .lectureHallCode(lecture.getLectureHallCode())
                    .lectureDate(DateUtil.dateToString(lecture.getLectureDate()))
                    .lectureContent(lecture.getLectureContent())
                    .applyCnt(lecture.getApplyCnt())
                    .maxCnt(lecture.getMaxCnt())
                    .regDate(DateUtil.dateToString(lecture.getRegDate()))
                    .build();
        }).collect(Collectors.toList());
        return new LectureDto.GetLecturesByEmployeeNumberRes(lectures);
    }

    public Lecture getLecture(Long lectureSeq) {
        // before
//        return lectureRepository.findById(lectureSeq)
//                .orElseThrow(() -> new Exception("등록되지않은 강연입니다."));

        // after
        return lectureRepository.findByIdWithPessimisticLock(lectureSeq);
    }

    public LectureDto.AddRes add(LectureDto.AddReq req) throws ParseException {
        Lecture lecture = new Lecture();
        lecture.setLecturer(req.getLecturer());
        lecture.setLectureHallCode(req.getLectureHallCode());
        lecture.setLectureDate(DateUtil.stringToDate(req.getLectureDate()));
        lecture.setLectureContent(req.getLectureContent());
        lecture.setApplyCnt(0);
        lecture.setMaxCnt(req.getMaxCnt());
        lecture.setRegDate(new Date());

        Lecture savedLecture = lectureRepository.save(lecture);
        return LectureDto.AddRes.builder()
                .lecturer(savedLecture.getLecturer())
                .lectureHallCode(savedLecture.getLectureHallCode())
                .lectureDate(DateUtil.dateToString(savedLecture.getLectureDate()))
                .lectureContent(savedLecture.getLectureContent())
                .maxCnt(savedLecture.getMaxCnt())
                .regDate(DateUtil.dateToString(savedLecture.getRegDate()))
                .build();
    }

}
