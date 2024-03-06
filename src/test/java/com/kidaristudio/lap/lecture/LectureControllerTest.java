package com.kidaristudio.lap.lecture;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kidaristudio.lap.apply.domain.ApplyDto;
import com.kidaristudio.lap.apply.service.ApplyService;
import com.kidaristudio.lap.common.constant.lecture.LectureHallCode;
import com.kidaristudio.lap.common.response.BaseResult;
import com.kidaristudio.lap.common.util.DateUtil;
import com.kidaristudio.lap.lecture.domain.Lecture;
import com.kidaristudio.lap.lecture.domain.LectureDto;
import com.kidaristudio.lap.lecture.service.LectureService;
import com.kidaristudio.lap.lecture.web.LectureController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {

    @InjectMocks
    private LectureController lectureController;

    @Mock
    private LectureService lectureService;

    @Mock
    private ApplyService applyService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
    }

    @DisplayName("강연 목록 조회 테스트")
    @Test
    void getLectures() throws Exception {
        // given
        doReturn(lecturesRes())
                .when(lectureService)
                .getLectures(any(LectureDto.GetLecturesReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/back-office/lectures")
                        .param("currentPage", "1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type type = new TypeToken<BaseResult<LectureDto.GetLecturesRes>>(){}.getType();
        BaseResult<LectureDto.GetLecturesRes> baseResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        LectureDto.GetLecturesRes lecturesRes = baseResult.getContents();
        System.out.println("lectures size >>> " + lecturesRes.getLectures().size());

        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
        assertThat(lecturesRes.getTotalCount()).isEqualTo(10);
        assertThat(lecturesRes.getRowCntPerPage()).isEqualTo(5);
    }

    @DisplayName("노출할 강연 목록 조회 테스트")
    @Test
    void getExposedLectures() throws Exception {
        // given
        doReturn(lecturesRes())
                .when(lectureService)
                .getExposedLectures(any(LectureDto.GetLecturesReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/front/lectures")
                        .param("currentPage", "1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type type = new TypeToken<BaseResult<LectureDto.GetLecturesRes>>(){}.getType();
        BaseResult<LectureDto.GetLecturesRes> baseResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        LectureDto.GetLecturesRes lecturesRes = baseResult.getContents();
        System.out.println("lectures size >>> " + lecturesRes.getLectures().size());

        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
        assertThat(lecturesRes.getTotalCount()).isEqualTo(10);
        assertThat(lecturesRes.getRowCntPerPage()).isEqualTo(5);
    }

    @DisplayName("인기 강연 목록 조회 테스트")
    @Test
    void getPopularLectures() throws Exception {
        // given
        doReturn(lecturesRes())
                .when(lectureService)
                .getPopularLectures(any(LectureDto.GetLecturesReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/front/popular-lectures")
                        .param("currentPage", "1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type type = new TypeToken<BaseResult<LectureDto.GetLecturesRes>>(){}.getType();
        BaseResult<LectureDto.GetLecturesRes> baseResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        LectureDto.GetLecturesRes lecturesRes = baseResult.getContents();
        System.out.println("lectures size >>> " + lecturesRes.getLectures().size());

        assertThat(lecturesRes.getLectures().size()).isEqualTo(5);
        assertThat(lecturesRes.getTotalCount()).isEqualTo(10);
        assertThat(lecturesRes.getRowCntPerPage()).isEqualTo(5);
    }

    private LectureDto.GetLecturesRes lecturesRes() {
        List<LectureDto.GetLecturesRes.Lecture> lectures = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            Lecture lecture = new Lecture();
            lecture.setLectureSeq(i);
            lecture.setLecturer("강연자" + i);
            lecture.setLectureHallCode(LectureHallCode.KS001);
            lecture.setLectureDate(new Date());
            lecture.setLectureContent("강연자" + i + "의 특별강연");
            lecture.setApplyCnt(0);
            lecture.setMaxCnt(100);
            lecture.setRegDate(new Date());

            LectureDto.GetLecturesRes.Lecture resultLecture = new LectureDto.GetLecturesRes.Lecture(lecture);
            lectures.add(resultLecture);
        }

        return LectureDto.GetLecturesRes.builder()
                .lectures(lectures)
                .totalCount(10L)
                .rowCntPerPage(5)
                .build();
    }

    @DisplayName("특정 강연의 신청자 목록 조회 테스트")
    @Test
    void getApplicants() throws Exception {
        // given
        doReturn(applicantsRes())
                .when(applyService)
                .getApplicants(any(ApplyDto.GetApplicantsReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/back-office/lectures/1/applicants" )
                        .param("currentPage", "1")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type type = new TypeToken<BaseResult<ApplyDto.GetApplicantsRes>>(){}.getType();
        BaseResult<ApplyDto.GetApplicantsRes> baseResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        ApplyDto.GetApplicantsRes applicantsRes = baseResult.getContents();
        System.out.println("applicants size >>> " + applicantsRes.getApplicants().size());

        assertThat(applicantsRes.getApplicants().size()).isEqualTo(5);
        assertThat(applicantsRes.getTotalCount()).isEqualTo(10);
        assertThat(applicantsRes.getRowCntPerPage()).isEqualTo(5);
    }

    private ApplyDto.GetApplicantsRes applicantsRes() {
        List<ApplyDto.GetApplicantsRes.Applicant> applicants = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            ApplyDto.GetApplicantsRes.Applicant applicant = ApplyDto.GetApplicantsRes.Applicant.builder()
                    .applySeq(i)
                    .employeeNumber("1111" + i)
                    .regDate(DateUtil.dateToString(new Date()))
                    .build();
            applicants.add(applicant);
        }

        return ApplyDto.GetApplicantsRes.builder()
                .applicants(applicants)
                .totalCount(10L)
                .rowCntPerPage(5)
                .build();
    }

    @DisplayName("강연 등록 테스트")
    @Test
    void add() throws Exception {
        // given
        LectureDto.AddReq addReq = addReq();

        doReturn(addRes())
                .when(lectureService)
                .add(any(LectureDto.AddReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/back-office/lecture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(addReq))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("contents.lecturer", addRes().getLecturer()).exists())
                .andExpect(jsonPath("contents.lectureHallCode", addRes().getLectureHallCode()).exists())
                .andReturn();
        System.out.println("addSuccess >>> " + mvcResult.getResponse().getContentAsString());
    }

    private LectureDto.AddReq addReq() {
        return LectureDto.AddReq.builder()
                .lecturer("강연자1")
                .lectureHallCode(LectureHallCode.KS001)
                .lectureDate(DateUtil.dateToString(new Date()))
                .lectureContent("강연자1의 특별강연")
                .maxCnt(100)
                .build();
    }

    private LectureDto.AddRes addRes() {
        return LectureDto.AddRes.builder()
                .lecturer("강연자1")
                .lectureHallCode(LectureHallCode.KS001)
                .lectureDate(DateUtil.dateToString(new Date()))
                .lectureContent("강연자1의 특별강연")
                .maxCnt(100)
                .regDate(DateUtil.dateToString(new Date()))
                .build();
    }

    @DisplayName("특정 직원이 신청한 강연 목록 조회 테스트")
    @Test
    void getLecturesByEmployeeNumber() throws Exception {
        // given
        doReturn(lecturesByEmployeeNumberRes())
                .when(lectureService)
                .getLectures(anyString());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/front/applicants/11111/lectures")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        Type type = new TypeToken<BaseResult<LectureDto.GetLecturesByEmployeeNumberRes>>(){}.getType();
        BaseResult<LectureDto.GetLecturesByEmployeeNumberRes> baseResult = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), type);
        LectureDto.GetLecturesByEmployeeNumberRes lecturesByEmployeeNumberRes = baseResult.getContents();

        System.out.println("lecturesByEmployeeNumberRes size >>> " + lecturesByEmployeeNumberRes.getLectures().size());
        assertThat(lecturesByEmployeeNumberRes.getLectures().size()).isEqualTo(5);
    }

    private LectureDto.GetLecturesByEmployeeNumberRes lecturesByEmployeeNumberRes() {
        List<LectureDto.GetLecturesByEmployeeNumberRes.Lecture> lectures = new ArrayList<>();
        for (long i = 0; i < 5; i++) {
            LectureDto.GetLecturesByEmployeeNumberRes.Lecture lecture = LectureDto.GetLecturesByEmployeeNumberRes.Lecture.builder()
                    .lectureSeq(i)
                    .lecturer("강연자" + i)
                    .lectureHallCode(LectureHallCode.KS001)
                    .lectureDate(DateUtil.dateToString(new Date()))
                    .lectureContent("강연자" + i + "의 특별강연")
                    .applyCnt(10)
                    .maxCnt(100)
                    .regDate(DateUtil.dateToString(new Date()))
                    .build();
            lectures.add(lecture);
        }

        return new LectureDto.GetLecturesByEmployeeNumberRes(lectures);
    }

}
