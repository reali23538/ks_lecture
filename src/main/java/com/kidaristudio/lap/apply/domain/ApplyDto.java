package com.kidaristudio.lap.apply.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ApplyDto {

    @Getter
    @Setter
    @Builder
    public static class GetApplicantsReq {
        @Schema(description = "현재 페이지")
        private Integer currentPage;

        @Schema(description = "강연")
        private Lecture lecture;

        @Getter
        @Setter
        public static class Lecture {
            @Schema(description = "강연Seq")
            private Long lectureSeq;

            public Lecture(Long lectureSeq) {
                this.lectureSeq = lectureSeq;
            }
        }
    }

    @Getter
    @Setter
    @Builder
    public static class GetApplicantsRes {
        @Schema(description = "신청 목록")
        private List<Applicant> applicants;

        @Schema(description = "신청 리스트 총개수")
        private Long totalCount;

        @Schema(description = "페이지당 row 개수")
        private Integer rowCntPerPage;

        @Getter
        @Setter
        @Builder
        public static class Applicant {
            @Schema(description = "신청Seq")
            private Long applySeq;

            @Schema(description = "사번", example = "10001")
            private String employeeNumber;

            @Schema(description = "등록일시(yyyyMMddHHmm)", example = "202403061930")
            private String regDate;

        }
    }

    @Schema(description = "강연 신청 요청값")
    @Getter
    @Setter
    @Builder
    public static class ApplyReq {
        @Schema(description = "강연", hidden = true)
        private Lecture lecture;

        @Schema(description = "사번", example = "10001")
        @NotBlank(message = "사번을 작성해주세요.")
        @Size(min = 5, max = 5, message = "사번이 잘못되었습니다.")
        private String employeeNumber;

        @Getter
        @Setter
        public static class Lecture {
            @Schema(description = "강연Seq")
            private Long lectureSeq;

            public Lecture(Long lectureSeq) {
                this.lectureSeq = lectureSeq;
            }
        }
    }

    @Getter
    @Setter
    @Builder
    public static class ApplyRes {
        @Schema(description = "신청Seq")
        private Long applySeq;

        @Schema(description = "등록일시(yyyyMMddHHmm)", example = "202403061930")
        private String regDate;
    }

}
