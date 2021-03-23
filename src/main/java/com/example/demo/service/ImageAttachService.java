package com.example.demo.service;

import com.example.demo.domain.ImageAttachVO;
import com.example.demo.mapper.ImageAttachMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageAttachService {
    @Setter(onMethod_ = @Autowired)
    ImageAttachMapper mapper;

    public boolean upload(ImageAttachVO vo) {
        return mapper.insert(vo) == 1;
    }
}
