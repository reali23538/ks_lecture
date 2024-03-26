package com.ks.lap.common.constant.lecture;

import lombok.Getter;

@Getter
public enum LectureHallCode {

	KS001("키다리 스튜디오 강연장1"),
	KS002("키다리 스튜디오 강연장2"),
	KS003("키다리 스튜디오 강연장3");

	private String description;

	LectureHallCode(String description) {
		this.description = description;
	}

}
