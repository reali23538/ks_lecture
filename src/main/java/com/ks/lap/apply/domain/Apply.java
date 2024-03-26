package com.ks.lap.apply.domain;

import com.ks.lap.lecture.domain.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "apply")
public class Apply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_seq")
    private Lecture lecture;

    private String employeeNumber;

    private Date regDate;

}
