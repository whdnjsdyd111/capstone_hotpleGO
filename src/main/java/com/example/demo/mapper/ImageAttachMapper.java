package com.example.demo.mapper;

import com.example.demo.domain.ImageAttachVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageAttachMapper {
    public int insert(ImageAttachVO vo);
}
