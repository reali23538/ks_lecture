package com.kidaristudio.lap.lecture.domain;

import com.kidaristudio.lap.common.constant.lecture.LectureHallCode;
import com.kidaristudio.lap.common.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LectureDto {

    @Getter
    @Setter
    public static class GetLecturesReq {
        @Schema(description = "현재 페이지")
        private Integer currentPage;

        public GetLecturesReq(Integer currentPage) {
            this.currentPage = currentPage;
        }
    }

    @Getter
    @Setter
    @Builder
    public static class GetLecturesRes {
        @Schema(description = "강연 목록")
        private List<Lecture> lectures;

        @Schema(description = "강연 리스트 총개수")
        private Long totalCount;

        @Schema(description = "페이지당 row 개수")
        private Integer rowCntPerPage;

        @Getter
        @Setter
        public static class Lecture {
            @Schema(description = "강연Seq")
            private Long lectureSeq;

            @Schema(description = "강연자", example = "김강연")
            private String lecturer;

            @Schema(description = "강연장")
            private LectureHallCode lectureHallCode;

            @Schema(description = "강연일시(yyyyMMddHHmm)", example = "202403061930")
            private String lectureDate;

            @Schema(description = "강연내용", example = "특별강연 입니다.")
            private String lectureContent;

            @Schema(description = "신청한 인원", example = "0")
            private Integer applyCnt;

            @Schema(description = "신청 가능 인원", example = "100")
            private Integer maxCnt;

            @Schema(description = "등록일시(yyyyMMddHHmm)", example = "202403061930")
            private String regDate;

            public Lecture(com.kidaristudio.lap.lecture.domain.Lecture lecture) {
                this.setLectureSeq(lecture.getLectureSeq());
                this.setLecturer(lecture.getLecturer());
                this.setLectureHallCode(lecture.getLectureHallCode());
                this.setLectureDate(DateUtil.dateToString(lecture.getLectureDate()));
                this.setLectureContent(lecture.getLectureContent());
                this.setApplyCnt(lecture.getApplyCnt());
                this.setMaxCnt(lecture.getMaxCnt());
                this.setRegDate(DateUtil.dateToString(lecture.getRegDate()));
            }
        }
    }

    @Schema(description = "강연 등록 요청값")
    @Getter
    @Setter
    @Builder
    public static class AddReq {
        @Schema(description = "강연자", example = "김강연")
        @NotBlank(message = "강연자를 작성해주세요.")
        private String lecturer;

        @Schema(description = "강연장")
        @NotNull(message = "강연장을 선택해주세요.")
        private LectureHallCode lectureHallCode;

        @Schema(description = "강연일시(yyyyMMddHHmm)", example = "202403061930")
        @NotBlank(message = "강연일시를 작성해주세요.")
        private String lectureDate;

        @Schema(description = "강연내용", example = "특별강연 입니다.")
        @NotBlank(message = "강연내용을 작성해주세요.")
        private String lectureContent;

        @Schema(description = "신청 가능 인원", example = "100")
        @NotNull(message = "신청 가능 인원을 작성해주세요.")
        private Integer maxCnt;
    }

    @Getter
    @Setter
    @Builder
    public static class AddRes {
        @Schema(description = "강연자", example = "김강연")
        private String lecturer;

        @Schema(description = "강연장")
        private LectureHallCode lectureHallCode;

        @Schema(description = "강연일시(yyyyMMddHHmm)", example = "202403061930")
        private String lectureDate;

        @Schema(description = "강연내용", example = "특별강연 입니다.")
        private String lectureContent;

        @Schema(description = "신청 가능 인원", example = "100")
        private Integer maxCnt;

        @Schema(description = "등록일시(yyyyMMddHHmm)", example = "202403061930")
        private String regDate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class GetLecturesByEmployeeNumberRes {
        @Schema(description = "강연 목록")
        private List<Lecture> lectures;

        @Getter
        @Setter
        @Builder
        public static class Lecture {
            @Schema(description = "강연Seq")
            private Long lectureSeq;

            @Schema(description = "강연자", example = "김강연")
            private String lecturer;

            @Schema(description = "강연장")
            private LectureHallCode lectureHallCode;

            @Schema(description = "강연일시(yyyyMMddHHmm)", example = "202403061930")
            private String lectureDate;

            @Schema(description = "강연내용", example = "특별강연 입니다.")
            private String lectureContent;

            @Schema(description = "신청한 인원", example = "0")
            private Integer applyCnt;

            @Schema(description = "신청 가능 인원", example = "100")
            private Integer maxCnt;

            @Schema(description = "등록일시(yyyyMMddHHmm)", example = "202403061930")
            private String regDate;
        }
    }

}
