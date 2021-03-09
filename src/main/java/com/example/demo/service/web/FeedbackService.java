package com.example.demo.service.web;

import com.example.demo.domain.web.AllianceVO;
import com.example.demo.domain.web.FeedbackVO;

import java.util.List;

public interface FeedbackService {

    /**
     *
     * @param code 기본키
     * @return FeedbackVO
     * 피드백을 선택 했을 때 해당되는 피드백 데이터 조회
     */
    public FeedbackVO get(String code);

    /**
     *
     * @return List AllianceVO
     * 관리자 피드백 처리 페이지에서 조회
     * 관리자가 아직 처리하지 않은 데이터
     */
    public List<FeedbackVO> getListN();

    /**
     *
     * @return List AllianceVO
     * 관리자 피드백 처리 페이지에서 조회
     * 관리자가 처리한 데이터
     */
    public List<FeedbackVO> getListY();

    /**
     * @param vo 피드백 내용
     * 피드백 페이지에서 등록
     */
    public boolean register(FeedbackVO vo);

    /**
     *
     * @param code 기본키
     * @return 바뀐 여부
     * 관리자가 미처리된 피드백을 확인하고 처리할 수 있는 기능
     */
    public boolean change(String code);

    /**
     *
     * @param code 기본키
     * @return 삭제 여부
     * 엉뚱하거나 이상한 피드백 건을 삭제할 수 있는 기능
     */
    public boolean remove(String code);
}
