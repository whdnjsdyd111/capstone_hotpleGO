package com.example.demo.controller.web;

import lombok.extern.log4j.Log4j2;
import org.osgeo.proj4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@Controller
public class JusoApiController {
    @GetMapping("/popup/jusoPopup")
    public String jusoPopup() {
        return "manager/jusoPopup";
    }

    @PostMapping(value = "/popup/jusoPopup")
    public String jusoPopup(HttpServletRequest request, Model model) {
        // request.setCharacterEncoding("EUC-KR");  //해당시스템의 인코딩타입이 EUC-KR일경우에
        // request.setCharacterEncoding("UTF-8");  //한글깨지면 주석제거
        String inputYn = request.getParameter("inputYn");
        String roadFullAddr = request.getParameter("roadFullAddr");
        String roadAddrPart1 = request.getParameter("roadAddrPart1");
        String roadAddrPart2 = request.getParameter("roadAddrPart2");
        String engAddr = request.getParameter("engAddr");
        String jibunAddr = request.getParameter("jibunAddr");
        String zipNo = request.getParameter("zipNo");
        String addrDetail = request.getParameter("addrDetail");
        String admCd    = request.getParameter("admCd");
        String rnMgtSn = request.getParameter("rnMgtSn");
        String bdMgtSn  = request.getParameter("bdMgtSn");
        // API 서비스 제공항목 확대 (2017.02)
        String detBdNmList  = request.getParameter("detBdNmList");
        String bdNm  = request.getParameter("bdNm");
        String bdKdcd  = request.getParameter("bdKdcd");
        String siNm  = request.getParameter("siNm");
        String sggNm  = request.getParameter("sggNm");
        String emdNm  = request.getParameter("emdNm");
        String liNm  = request.getParameter("liNm");
        String rn  = request.getParameter("rn");
        String udrtYn  = request.getParameter("udrtYn");
        String buldMnnm  = request.getParameter("buldMnnm");
        String buldSlno  = request.getParameter("buldSlno");
        String mtYn  = request.getParameter("mtYn");
        String lnbrMnnm  = request.getParameter("lnbrMnnm");
        String lnbrSlno  = request.getParameter("lnbrSlno");
        String emdNo  = request.getParameter("emdNo");
        String entX  = request.getParameter("entX");
        String entY  = request.getParameter("entY");


        model.addAttribute("inputYn", inputYn);
        model.addAttribute("roadFullAddr", roadFullAddr);
        model.addAttribute("roadAddrPart1", roadAddrPart1);
        model.addAttribute("roadAddrPart2", roadAddrPart2);
        model.addAttribute("engAddr", engAddr);
        model.addAttribute("jibunAddr", jibunAddr);
        model.addAttribute("zipNo", zipNo);
        model.addAttribute("addrDetail", addrDetail);
        model.addAttribute("admCd", admCd);
        model.addAttribute("rnMgtSn", rnMgtSn);
        model.addAttribute("bdMgtSn", bdMgtSn);
        model.addAttribute("detBdNmList", detBdNmList);
        model.addAttribute("bdNm", bdNm);
        model.addAttribute("bdKdcd", bdKdcd);
        model.addAttribute("siNm", siNm);
        model.addAttribute("sggNm", sggNm);
        model.addAttribute("emdNm", emdNm);
        model.addAttribute("liNm", liNm);
        model.addAttribute("rn", rn);
        model.addAttribute("udrtYn", udrtYn);
        model.addAttribute("buldMnnm", buldMnnm);
        model.addAttribute("buldSlno", buldSlno);
        model.addAttribute("mtYn", mtYn);
        model.addAttribute("lnbrMnnm", lnbrMnnm);
        model.addAttribute("lnbrSlno", lnbrSlno);
        model.addAttribute("emdNo", emdNo);
        model.addAttribute("entX", entX);
        model.addAttribute("entY", entY);

        ProjCoordinate trans = transform(Double.parseDouble(entX), Double.parseDouble(entY));
        log.info("경도 lng : " + trans.x);
        log.info("위도 lat : " + trans.y);

        model.addAttribute("lng", trans.x);
        model.addAttribute("lat", trans.y);

        log.info("\ninput : " + inputYn + "\n"+
                "roadFullAddr : " + roadFullAddr + "\n" +
                "roadAddrPart1 : " + roadAddrPart1 + "\n" +
                "roadAddrPart2 : " + roadAddrPart2 + "\n" +
                "engAddr : " + engAddr + "\n" +
                "jibunAddr : " + jibunAddr + "\n" +
                "zipNo : " + zipNo + "\n" +
                "addrDetail : " + addrDetail + "\n" +
                "admCd : " + admCd + "\n" +
                "rnMgtSn : " + rnMgtSn + "\n" +
                "bdMgtSn : " + bdMgtSn + "\n" +
                "detBdNmList : " + detBdNmList + "\n" +
                "bdNm : " + bdNm + "\n" +
                "bdKdcd : " + bdKdcd + "\n" +
                "siNm : " + siNm + "\n" +
                "sggNm : " + sggNm + "\n" +
                "emdNm : " + emdNm + "\n" +
                "liNm : " + liNm + "\n" +
                "rn : " + rn + "\n" +
                "udrtYn : " + udrtYn + "\n" +
                "buldMnnm : " + buldMnnm + "\n" +
                "buldSlno : " + buldSlno + "\n" +
                "mtYn : " + mtYn + "\n" +
                "lnbrMnnm : " + lnbrMnnm + "\n" +
                "lnbrSlno : " + lnbrSlno + "\n" +
                "emdNo : " + emdNo + "\n" +
                "entX : " + entX + "\n" +
                "entY : " + entY);

        return "manager/jusoPopup";
    }

    private ProjCoordinate transform(Double x, Double y) {
        CRSFactory factory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = factory.createFromName("EPSG:5179");
        CoordinateReferenceSystem dstCrs = factory.createFromName("EPSG:4326");

        BasicCoordinateTransform transform = new BasicCoordinateTransform(srcCrs, dstCrs);

        ProjCoordinate srcCoord = new ProjCoordinate(x, y);
        ProjCoordinate dstCoord = new ProjCoordinate();

        return transform.transform(srcCoord, dstCoord);
    }
}