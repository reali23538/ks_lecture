package com.ks.lap.lecture.domain;

import com.ks.lap.apply.domain.Apply;
import com.ks.lap.common.constant.lecture.LectureHallCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "lecture")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureSeq;

    private String lecturer;

    @Enumerated(EnumType.STRING)
    private LectureHallCode lectureHallCode;

    private Date lectureDate;

    private String lectureContent;

    private Integer applyCnt;

    private Integer maxCnt;

    private Date regDate;

    @OneToMany(mappedBy = "lecture")
    private List<Apply> applies;

    public Lecture(Long lectureSeq) { this.lectureSeq = lectureSeq; }

}
