package com.kidaristudio.lap.lecture.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLecture is a Querydsl query type for Lecture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLecture extends EntityPathBase<Lecture> {

    private static final long serialVersionUID = -902419856L;

    public static final QLecture lecture = new QLecture("lecture");

    public final ListPath<com.kidaristudio.lap.apply.domain.Apply, com.kidaristudio.lap.apply.domain.QApply> applies = this.<com.kidaristudio.lap.apply.domain.Apply, com.kidaristudio.lap.apply.domain.QApply>createList("applies", com.kidaristudio.lap.apply.domain.Apply.class, com.kidaristudio.lap.apply.domain.QApply.class, PathInits.DIRECT2);

    public final NumberPath<Integer> applyCnt = createNumber("applyCnt", Integer.class);

    public final StringPath lectureContent = createString("lectureContent");

    public final DateTimePath<java.util.Date> lectureDate = createDateTime("lectureDate", java.util.Date.class);

    public final EnumPath<com.kidaristudio.lap.common.constant.lecture.LectureHallCode> lectureHallCode = createEnum("lectureHallCode", com.kidaristudio.lap.common.constant.lecture.LectureHallCode.class);

    public final StringPath lecturer = createString("lecturer");

    public final NumberPath<Long> lectureSeq = createNumber("lectureSeq", Long.class);

    public final NumberPath<Integer> maxCnt = createNumber("maxCnt", Integer.class);

    public final DateTimePath<java.util.Date> regDate = createDateTime("regDate", java.util.Date.class);

    public QLecture(String variable) {
        super(Lecture.class, forVariable(variable));
    }

    public QLecture(Path<? extends Lecture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLecture(PathMetadata metadata) {
        super(Lecture.class, metadata);
    }

}

