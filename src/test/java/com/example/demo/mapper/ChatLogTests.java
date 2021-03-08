package com.example.demo.mapper;

import com.example.demo.domain.web.ChatLogVO;
import com.example.demo.mapper.web.ChatLogMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class ChatLogTests {
    @Setter(onMethod_ = @Autowired)
    ChatLogMapper mapper;

    @Test
    public void test() {
        ChatLogVO vo = ChatLogVO.builder().chtCont("ddz").uCode("whdnjsdyd111@naver.com/A/").build();
        log.info(vo);
        log.info(mapper.insertChat(vo));
        log.info(vo);
        log.info(vo.getChtCode());
    }
}
