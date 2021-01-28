package com.example.demo.service;

import com.example.demo.domain.AllianceVO;

import java.util.List;

public interface AllianceService {

    /**
     *
     * @return List AllianceVO
     * 관리자 제휴 처리 페이지에서 조회
     * 관리자가 아직 처리하지 않은 데이터
     */
    public List<AllianceVO> getListN();

    /**
     *
     * @return List AllianceVO
     * 관리자 제휴 처리 페이지에서 조회
     * 관리자가 처리한 데이터
     */
    public List<AllianceVO> getListY();

    /**
     * @param vo 이름, 이메일, 전화번호, 내용 입력
     * @return 0 or 1
     * 제휴 페이지에서 등록
     */
    public int register(AllianceVO vo);
}
