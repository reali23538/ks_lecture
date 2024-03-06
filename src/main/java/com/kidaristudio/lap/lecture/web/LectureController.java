package com.kidaristudio.lap.lecture.web;

import com.kidaristudio.lap.apply.domain.ApplyDto;
import com.kidaristudio.lap.apply.service.ApplyService;
import com.kidaristudio.lap.common.exception.NoApplyLectureException;
import com.kidaristudio.lap.common.response.BaseResponse;
import com.kidaristudio.lap.common.response.BaseResult;
import com.kidaristudio.lap.common.response.code.BaseResultCode;
import com.kidaristudio.lap.lecture.domain.LectureDto;
import com.kidaristudio.lap.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Tag(name = "Lecture", description = "강연 API")
@RestController
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    private final ApplyService applyService;

    @Operation(summary = "강연 목록", description = "전체 강연 목록을 조회하는 API 입니다.")
    @GetMapping("/back-office/lectures")
    public ResponseEntity<BaseResult<LectureDto.GetLecturesRes>> getLectures(
            @Parameter(name = "currentPage", description = "현재 페이지")
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) {
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(currentPage);
        return BaseResponse.ok(lectureService.getLectures(req));
    }

    @Operation(summary = "강연 신청자 목록", description = "강연별 신청자 목록을 조회하는 API 입니다.")
    @GetMapping("/back-office/lectures/{lectureSeq}/applicants")
    public ResponseEntity<BaseResult<ApplyDto.GetApplicantsRes>> getApplicants(
            @Parameter(name = "lectureSeq", description = "강연 Seq", in = ParameterIn.PATH)
            @PathVariable(name = "lectureSeq") Long lectureSeq,
            @Parameter(name = "currentPage", description = "현재 페이지")
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) {
        ApplyDto.GetApplicantsReq req = ApplyDto.GetApplicantsReq.builder()
                .currentPage(currentPage)
                .lecture(new ApplyDto.GetApplicantsReq.Lecture(lectureSeq))
                .build();
        return BaseResponse.ok(applyService.getApplicants(req));
    }

    @Operation(summary = "강연 등록", description = "강연을 등록하는 API 입니다.")
    @PostMapping("/back-office/lecture")
    public ResponseEntity<BaseResult<LectureDto.AddRes>> add(@RequestBody @Valid LectureDto.AddReq req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseResponse.getResponseEntity(BaseResultCode.COMMON_INVALID_PARAMS, bindingResult.getAllErrors());
        }

        try {
            return BaseResponse.ok(lectureService.add(req));
        } catch (ParseException e) {
            return BaseResponse.fail(BaseResultCode.COMMON_PARSE_EXCEPTION);
        }
    }

    @Operation(summary = "강연 목록", description = "강연 목록을 조회하는 API 입니다.")
    @GetMapping("/front/lectures")
    public ResponseEntity<BaseResult<LectureDto.GetLecturesRes>> getExposedLectures(
            @Parameter(name = "currentPage", description = "현재 페이지")
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) {
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(currentPage);
        return BaseResponse.ok(lectureService.getExposedLectures(req));
    }

    @Operation(summary = "인기 강연 목록", description = "실시간 인기 강연을 조회하는 API 입니다.")
    @GetMapping("/front/popular-lectures")
    public ResponseEntity<BaseResult<LectureDto.GetLecturesRes>> getPopularLectures(
            @Parameter(name = "currentPage", description = "현재 페이지")
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage) {
        LectureDto.GetLecturesReq req = new LectureDto.GetLecturesReq(currentPage);
        return BaseResponse.ok(lectureService.getPopularLectures(req));
    }

    @Operation(summary = "신청 내역 조회", description = "신청한 강연 목록을 조회하는 API 입니다.")
    @GetMapping("/front/applicants/{employeeNumber}/lectures")
    public ResponseEntity<BaseResult<LectureDto.GetLecturesByEmployeeNumberRes>> getLecturesByEmployeeNumber(
            @Parameter(name = "employeeNumber", description = "사번", in = ParameterIn.PATH)
            @PathVariable(name = "employeeNumber") String employeeNumber) {
        try {
            return BaseResponse.ok(lectureService.getLectures(employeeNumber));
        } catch (NoApplyLectureException noApplyLectureException) {
            return BaseResponse.fail(BaseResultCode.NO_APPLY_LECTURE);
        }
    }

}
