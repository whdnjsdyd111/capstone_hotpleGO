package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    public String countUsers(String count);

    public String countBoard(String count);

    public String countComm(String count);

    public String countCourse(String count);

    public String countReport(String count);

    public String countAlc(String count);

    public int deleteContent(String bdCode);
}
