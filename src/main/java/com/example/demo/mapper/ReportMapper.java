package com.example.demo.mapper;

import com.example.demo.domain.ReportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    public int reportBoard(ReportVO reportVO);

    public int reportComment(ReportVO reportVO);

    public List<ReportVO> getReportList();

    public int deleteReport(String repCode);
}
