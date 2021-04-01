package com.example.demo.mapper;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.mapper.web.BoardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTests {
    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void testOfInset(){
        BoardVO vo = new BoardVO();
        vo.setBdCont("11");
        vo.setBdTitle("1 제목");
        vo.setBdCont("1 내용");
        vo.setUCode("tester");
        int result = boardMapper.insertBoard(vo);
        System.out.println(result);
    }
}
