package com.kidaristudio.lap.apply;

import com.google.gson.Gson;
import com.kidaristudio.lap.apply.domain.ApplyDto;
import com.kidaristudio.lap.apply.service.ApplyService;
import com.kidaristudio.lap.apply.web.ApplyController;
import com.kidaristudio.lap.common.util.DateUtil;
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

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ApplyControllerTest {

    @InjectMocks
    private ApplyController applyController;

    @Mock
    private ApplyService applyService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(applyController).build();
    }

    @DisplayName("강연 신청 테스트")
    @Test
    void apply() throws Exception {
        // given
        ApplyDto.ApplyReq applyReq = applyReq();

        doReturn(applyRes())
                .when(applyService)
                .apply(any(ApplyDto.ApplyReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/front/lectures/1/applicant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(applyReq))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("contents.applySeq", applyRes().getApplySeq()).exists())
                .andExpect(jsonPath("contents.regDate", applyRes().getRegDate()).exists())
                .andReturn();
        System.out.println("applySuccess >>> " + mvcResult.getResponse().getContentAsString());
    }

    private ApplyDto.ApplyReq applyReq() {
        return ApplyDto.ApplyReq.builder()
                .employeeNumber("11111")
                .build();
    }

    private ApplyDto.ApplyRes applyRes() {
        return ApplyDto.ApplyRes.builder()
                .applySeq(1L)
                .regDate(DateUtil.dateToString(new Date()))
                .build();
    }

    @DisplayName("강연 신청 취소 테스트")
    @Test
    void cancel() throws Exception {
        // given
        doNothing()
                .when(applyService)
                .cancel(any(ApplyDto.ApplyReq.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/front/lectures/1/applicants/11111")
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andReturn();
        System.out.println("cancel >>> " + mvcResult.getResponse().getContentAsString());
    }

}
