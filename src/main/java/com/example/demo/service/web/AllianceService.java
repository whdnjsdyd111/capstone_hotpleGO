package com.example.demo.service.web;

import com.example.demo.domain.web.AllianceVO;
import org.springframework.stereotype.Component;

import java.util.List;

public interface AllianceService {

    /**
     *
     * @param code 기본키
     * @return AllianceVO
     * 제휴를 선택 했을 때 해당되는 제휴 데이터 조회
     */
    public AllianceVO get(String code);

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
     * 제휴 페이지에서 등록
     */
    public boolean register(AllianceVO vo);

    /**
     *
     * @param code 기본키
     * @return 바뀐 여부
     * 관리자가 미처리된 제휴를 확인하고 처리할 수 있는 기능
     */
    public boolean change(String code);

    /**
     *
     * @param code 기본키
     * @return 삭제 여부
     * 엉뚱하거나 이상한 제휴건을 삭제할 수 있는 기능
     */
    public boolean remove(String code);
}