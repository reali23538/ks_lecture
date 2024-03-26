package com.ks.lap.apply;

import com.ks.lap.apply.domain.ApplyDto;
import com.ks.lap.apply.service.ApplyService;
import com.ks.lap.lecture.domain.Lecture;
import com.ks.lap.lecture.repository.LectureRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ApplyConcurrencyTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ApplyService applyService;

    @DisplayName("강연 신청시 동시성 테스트")
    @Test
    void apply() throws InterruptedException {
        // given
        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 10000; i < 10000 + threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    ApplyDto.ApplyReq req = ApplyDto.ApplyReq.builder()
                            .lecture(new ApplyDto.ApplyReq.Lecture(1L))
                            .employeeNumber(String.valueOf(finalI))
                            .build();
                    applyService.apply(req);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        final Lecture lecture = lectureRepository.findById(1L).orElseThrow();
        System.out.println("applyCnt >>> " + lecture.getApplyCnt());

        // then
        assertThat(lecture.getApplyCnt()).isEqualTo(100);
    }

    @DisplayName("강연 취소시 동시성 테스트")
    @Test
    void cancel() throws InterruptedException {
        // given
        final int threadCount = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(32);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 10000; i < 10000 + threadCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    ApplyDto.ApplyReq req = ApplyDto.ApplyReq.builder()
                            .lecture(new ApplyDto.ApplyReq.Lecture(1L))
                            .employeeNumber(String.valueOf(finalI))
                            .build();
                    applyService.cancel(req);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        final Lecture lecture = lectureRepository.findById(1L).orElseThrow();
        System.out.println("applyCnt >>> " + lecture.getApplyCnt());

        // then
        assertThat(lecture.getApplyCnt()).isEqualTo(0);
    }

}
