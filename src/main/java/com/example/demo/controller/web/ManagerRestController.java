package com.example.demo.controller.web;

import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.security.CustomUser;
import com.example.demo.service.HotpleService;
import com.example.demo.service.ImageAttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/manager/rest/*")
@RequiredArgsConstructor
@Log4j2
public class ManagerRestController {
    private final HotpleService hotple;
    private final ImageAttachService imageAttach;

    @PostMapping(value = "/comp-erm")
    @ResponseBody
    public ResponseEntity<String> companyEnrollment(HotpleVO vo, @AuthenticationPrincipal CustomUser manager,
                                                    MultipartFile upload) {
        ImageAttachVO imageAttachVO = new ImageAttachVO();
        if (upload != null) {
            imageAttachVO.upload(upload);
            if (imageAttach.upload(imageAttachVO)) {
                vo.setHtImg(imageAttachVO.getUuid());
            }
        }
        HotpleVO ht = hotple.getIdByAddr(vo.getHtAddr());
        vo.setUCode(manager.getUsername() + "/" + manager.getAuthorities().toArray()[0] + "/");

        if (ht != null) {
            if (ht.getBusnNum() != null) {
                return new ResponseEntity<>("이미 등록된 업체입니다.", HttpStatus.BAD_REQUEST);
            }
            if (hotple.registerBusnGo(vo)) {
                return new ResponseEntity<>("등록 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("등록 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }

        } else {
            if (hotple.registerBusn(vo)) {
                return new ResponseEntity<>("등록 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("등록 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PostMapping("/comp-update")
    @ResponseBody
    public ResponseEntity<String> companyUpdate(HotpleVO vo, MultipartFile upload) {
        ImageAttachVO imageAttachVO = new ImageAttachVO();
        if (upload != null) {
            imageAttachVO.upload(upload);
            hotple.updateWithImage(vo, imageAttachVO);
            imageAttachVO.deleteFiles(vo.getHtImg(), vo.getUploadPath(), vo.getFileName());
        } else {
            if (!hotple.update(vo)) {
                return new ResponseEntity<>("다시 시도해 주십시오.", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("수정 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/comp-delete")
    @ResponseBody
    public ResponseEntity<String> companyDelete(@RequestBody HotpleVO vo) {
        if (!hotple.remove(vo)) {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("삭제 완료하였습니다.", HttpStatus.OK);
    }

    @PostMapping("/menu-add")
    @ResponseBody
    public ResponseEntity<MenuVO> menuAdd(MenuVO vo, MultipartFile upload) {
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }
}
