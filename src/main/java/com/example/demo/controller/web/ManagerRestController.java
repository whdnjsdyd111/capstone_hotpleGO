package com.example.demo.controller.web;

import com.example.demo.domain.*;
import com.example.demo.security.CustomUser;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manager/rest/*")
@RequiredArgsConstructor
@Log4j2
public class ManagerRestController {
    private final HotpleService hotple;
    private final ImageAttachService imageAttach;
    private final MenuService menu;
    private final UserService user;
    private final OpenInfoService openInfo;
    private final ReviewService review;
    private final PasswordEncoder passwordEncoder;

    @Transactional
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

    @Transactional
    @PostMapping("/comp-update")
    @ResponseBody
    public ResponseEntity<String> companyUpdate(HotpleVO vo, MultipartFile upload) {
        log.info(vo);
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

    @Transactional
    @PostMapping("/menu-add")
    @ResponseBody
    public ResponseEntity<MenuVO> menuAdd(MenuVO vo, MultipartFile upload) {
        if (vo.getMeCode().isEmpty() || vo.getMeCate().isEmpty() || vo.getMeHashTag().isEmpty() || vo.getMeIntr().isEmpty() ||
                vo.getMeName().isEmpty() || vo.getMePrice() < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        ImageAttachVO imageAttachVO = new ImageAttachVO();
        if (upload != null) {
            imageAttachVO.upload(upload);
            if (imageAttach.upload(imageAttachVO)) {
                vo.setUuid(imageAttachVO.getUuid());
                vo.setUploadPath(imageAttachVO.getUploadPath());
                vo.setFileName(imageAttachVO.getFileName());
            }
        }
        if (menu.register(vo)) {
            log.info(vo);
            return new ResponseEntity<>(vo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PostMapping("/menu-update")
    @ResponseBody
    public ResponseEntity<MenuVO> menuUpdate(MenuVO vo, MultipartFile upload) {
        if (vo.getMeCode().isEmpty() || vo.getMeCate().isEmpty() || vo.getMeHashTag().isEmpty() || vo.getMeIntr().isEmpty() ||
                vo.getMeName().isEmpty() || vo.getMePrice() < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (upload == null) {
            if (menu.update(vo)) {
                return new ResponseEntity<>(vo, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } else {
            ImageAttachVO imageAttachVO = new ImageAttachVO();
            imageAttachVO.upload(upload);

            if (vo.getUuid().isEmpty()) {
                if (imageAttach.upload(imageAttachVO)) {
                    vo.setUuid(imageAttachVO.getUuid());
                    vo.setUploadPath(imageAttachVO.getUploadPath());
                    vo.setFileName(imageAttachVO.getFileName());
                    if (!menu.updateOnlyImage(vo)) {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                }
            } else {
                menu.updateWithImage(vo, imageAttachVO);
                imageAttachVO.deleteFiles(vo.getUuid(), vo.getUploadPath(), vo.getFileName());
                vo.setUuid(imageAttachVO.getUuid());
                vo.setUploadPath(imageAttachVO.getUploadPath());
                vo.setFileName(imageAttachVO.getFileName());
            }
            return new ResponseEntity<>(vo, HttpStatus.OK);
        }
    }

    @PostMapping("/cate-update")
    public ResponseEntity<String> categoryUpdate(HttpServletRequest request) {
        String before = request.getParameter("before");
        String category = request.getParameter("category");
        if (menu.updateCategory("5", before, category)) {
            return new ResponseEntity<>("일괄 수정하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete-cate")
    public ResponseEntity<String> cateDelete(HttpServletRequest request) {
        String category = request.getParameter("category");
        if (menu.removeCategory("5", category)) {
            return new ResponseEntity<>("일괄 삭제하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete-menu")
    public ResponseEntity<String> menuDelete(@RequestBody MenuVO vo) {
        log.info(vo);
        boolean img = vo.getUuid() != null && (!vo.getUuid().isEmpty());
        if (menu.remove(vo.getMeCode(), img)) {
            if (img) {
                ImageAttachVO image = new ImageAttachVO();
                image.deleteFiles(vo.getUuid(), vo.getUploadPath(), vo.getFileName());
            }
            return new ResponseEntity<>("메뉴 삭제하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setting-nick")
    public ResponseEntity<String> settingNick(@RequestBody ManagerVO vo, @AuthenticationPrincipal CustomUser manager) {
        if (user.updateNick(vo.getNick(), manager.user.getUCode())) {
            return new ResponseEntity<>("닉네임 변경 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setting-account")
    public ResponseEntity<String> settingAccount(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String mBank = request.getParameter("mBank");
        String mAccout = request.getParameter("mAccount");
        ManagerVO vo = new ManagerVO();
        vo.setMBank(mBank);
        vo.setMAccount(mAccout);
        vo.setUCode(manager.user.getUCode());
        log.info(vo);
        if (user.updateAccount(vo)) {
            return new ResponseEntity<>("계좌 변경 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/setting-password")
    public ResponseEntity<String> settingPassword(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String pw = request.getParameter("password");
        String new_pw = request.getParameter("new_password");
        String code = manager.user.getUCode();
        if (new BCryptPasswordEncoder().matches(pw, user.getPw(code))) {
            if (user.updatePw(passwordEncoder.encode(new_pw), code)) {
                return new ResponseEntity<>("비밀번호 변경 완료하였습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("변경 실패하였습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/change-show")
    public ResponseEntity<String> changeShow() {
        // TODO
        return null;
    }

    @PostMapping("/change-noShow")
    public ResponseEntity<String> changeNoShow() {
        // TODO
        return null;
    }

    @PostMapping("/save-weekday")
    public ResponseEntity<String> saveWeekday(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        String wo = request.getParameter("wost").replace(":", "") + "/" + request.getParameter("woet").replace(":", "");
        String wb = request.getParameter("wbst").replace(":", "") + "/" + request.getParameter("wbet").replace(":", "");
        if (openInfo.mergeOpen(wo, uCode, OpenInfoService.WEEKDAY) && openInfo.mergeBreak(wb, uCode, OpenInfoService.WEEKDAY)) {
            return new ResponseEntity<>("저장 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-weekend-sat")
    public ResponseEntity<String> saveWeekendSat(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        String wo = request.getParameter("wost").replace(":", "") + "/" + request.getParameter("woet").replace(":", "");
        String wb = request.getParameter("wbst").replace(":", "") + "/" + request.getParameter("wbet").replace(":", "");
        if (openInfo.mergeOpen(wo, uCode, OpenInfoService.SATURDAY) && openInfo.mergeBreak(wb, uCode, OpenInfoService.SATURDAY)) {
            return new ResponseEntity<>("저장 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save-weekend-sun")
    public ResponseEntity<String> saveWeekendSun(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        String wo = request.getParameter("wost").replace(":", "") + "/" + request.getParameter("woet").replace(":", "");
        String wb = request.getParameter("wbst").replace(":", "") + "/" + request.getParameter("wbet").replace(":", "");
        if (openInfo.mergeOpen(wo, uCode, OpenInfoService.SUNDAY) && openInfo.mergeBreak(wb, uCode, OpenInfoService.SUNDAY)) {
            return new ResponseEntity<>("저장 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/save-holiday")
    public ResponseEntity<String> saveHoliday(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        String whatWeek = request.getParameter("whatWeek");
        String whatDay = request.getParameter("whatDay");
        if (openInfo.insertHoliday(whatWeek, whatDay, uCode)) {
            return new ResponseEntity<>("저장 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete-holiday")
    public ResponseEntity<String> deleteHoliday(HttpServletRequest request, @AuthenticationPrincipal CustomUser manager) {
        String uCode = manager.user.getUCode();
        String code = request.getParameter("code");
        if (openInfo.deleteHoliday(code, uCode)) {
            return new ResponseEntity<>("삭제 완료하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/save-reviewReply")
    @ResponseBody
    public ResponseEntity<String> saveReviewReply(@RequestBody ReviewVO vo) {
        if (review.changeRvOwnCont(vo)) {
            return new ResponseEntity<>("답글을 달았습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete-reviewReply")
    @ResponseBody
    public ResponseEntity<String> deleteReviewReply(@RequestBody ReviewVO vo) {
        if (review.changeRvOwnCont(vo)) {
            return new ResponseEntity<>("답글을 삭제하였습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("다시 시도해주십시오.", HttpStatus.BAD_REQUEST);
        }
    }
}
