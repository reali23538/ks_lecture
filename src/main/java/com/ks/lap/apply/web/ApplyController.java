package com.ks.lap.apply.web;

import com.ks.lap.apply.domain.ApplyDto;
import com.ks.lap.apply.service.ApplyService;
import com.ks.lap.common.exception.AppliedLectureException;
import com.ks.lap.common.exception.NotAppliedLectureException;
import com.ks.lap.common.exception.SoldOutLectureException;
import com.ks.lap.common.response.BaseResponse;
import com.ks.lap.common.response.BaseResult;
import com.ks.lap.common.response.code.BaseResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Apply", description = "신청 API")
@RestController
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "강연 신청", description = "강연을 신청하는 API 입니다.")
    @PostMapping("/front/lectures/{lectureSeq}/applicant")
    public ResponseEntity<BaseResult<ApplyDto.ApplyRes>> apply(
            @Parameter(name = "lectureSeq", description = "강연 Seq", in = ParameterIn.PATH)
            @PathVariable(name = "lectureSeq") Long lectureSeq,
            @RequestBody @Valid ApplyDto.ApplyReq req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseResponse.getResponseEntity(BaseResultCode.COMMON_INVALID_PARAMS, bindingResult.getAllErrors());
        }
        req.setLecture(new ApplyDto.ApplyReq.Lecture(lectureSeq));

        try {
            return BaseResponse.ok(applyService.apply(req));
        } catch (AppliedLectureException e) {
            return BaseResponse.fail(BaseResultCode.APPLIED_LECTURE);
        } catch (SoldOutLectureException e) {
            return BaseResponse.fail(BaseResultCode.SOLD_OUT_LECTURE);
        }
    }

    @Operation(summary = "강연 취소", description = "신청한 강연을 취소하는 API 입니다.")
    @DeleteMapping("/front/lectures/{lectureSeq}/applicants/{employeeNumber}")
    public ResponseEntity<?> cancel(
            @Parameter(name = "lectureSeq", description = "강연 Seq", in = ParameterIn.PATH)
            @PathVariable(name = "lectureSeq") Long lectureSeq,
            @Parameter(name = "employeeNumber", description = "사번", in = ParameterIn.PATH)
            @PathVariable(name = "employeeNumber") String employeeNumber) {
        ApplyDto.ApplyReq req = ApplyDto.ApplyReq.builder()
                .lecture(new ApplyDto.ApplyReq.Lecture(lectureSeq))
                .employeeNumber(employeeNumber)
                .build();
        try {
            applyService.cancel(req);
            return BaseResponse.ok();
        } catch (NotAppliedLectureException e) {
            return BaseResponse.fail(BaseResultCode.NOT_APPLIED_LECTURE);
        }
    }
}
