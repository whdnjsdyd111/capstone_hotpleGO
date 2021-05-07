package com.example.demo.service;

import com.example.demo.domain.ReportVO;
import com.example.demo.mapper.ReportMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ReportService {
    @Setter(onMethod_ = @Autowired)
    private ReportMapper mapper;

    public boolean reportBoard(ReportVO reportVO) {
        return mapper.reportBoard(reportVO) == 1;
    }

    public boolean reportComment(ReportVO reportVO){
        return mapper.reportComment(reportVO) == 1;
    }

    public List<ReportVO> getReportList(){
        return mapper.getReportList();
    }

    public boolean deleteReport(String repCode){
        return mapper.deleteReport(repCode) == 1;
    }

}
