package edu.vegetable.controller;

import com.alibaba.fastjson.JSONObject;
import edu.vegetable.constants.Constants;
import edu.vegetable.model.*;
import edu.vegetable.service.*;
import edu.vegetable.utils.CustomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "/")
public class MobileController extends BaseController {

    private final VegeInfoService vegeService;

    private final ImageService imageService;

    private final CompanyService companyService;

    private final StandardService standardService;

    private final SymptomService symptomService;

    private final CureService cureService;

    @Autowired
    public MobileController(VegeInfoService vegeService, ImageService imageService, CompanyService companyService, StandardService standardService,SymptomService symptomService,CureService cureService) {
        this.vegeService = vegeService;
        this.imageService = imageService;
        this.companyService = companyService;
        this.standardService = standardService;
        this.symptomService = symptomService;
        this.cureService = cureService;
    }

    @RequestMapping(value = "/mobile_vegelist.json")
    public Map mobile_vegelist() {

        //获得query
        Map<String, String> condition = new HashMap<>();
        condition.put("page","1");
        condition.put("size","8");
        Page<VegeInfo> result = vegeService.query(condition);
        List<VegeInfo> vegeInfoList = result.getContent();
        List<MobileVege> mobileVegeList = new ArrayList<>();
        for(VegeInfo vegeInfo:vegeInfoList){
            MobileVege mobileVege = new MobileVege();
            mobileVege.setVegeName(vegeInfo.getVegeName());
            //建立图片
            String imgPath = imageService.queryPathByUuid(vegeInfo.getImgUuid());
            mobileVege.setImgPath("http://8.142.64.137:8080/show_img?imgPath="+imgPath);
            mobileVege.setVegeId(vegeInfo.getVegeId());
            mobileVegeList.add(mobileVege);
        }
        JSONObject data = new JSONObject();
        data.put("mobileVegeList",mobileVegeList);
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_vegeinfobyname.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryByName(HttpServletRequest request) {
        String vegeName = request.getParameter("vegeName");
        VegeInfo vegeInfo = vegeService.queryByName(vegeName);
        if(vegeInfo==null){
            JSONObject data = new JSONObject();
            data.put("msg","没有这种蔬菜！");
            return buildResponse(data);
        }
        JSONObject data = new JSONObject();
        data.put("vegeId", vegeInfo.getVegeId());
        data.put("vegeName", vegeInfo.getVegeName());
        data.put("alias", vegeInfo.getAlias());
        String imgPath = "";
        imgPath = imageService.queryPathByUuid(vegeInfo.getImgUuid());
        data.put("imgPath", "http://8.142.64.137:8080/show_img?imgPath="+imgPath);
        data.put("introduction", vegeInfo.getIntroduction());
        data.put("classification", Constants.VEGE_CLASS_MAP.get(vegeInfo.getClassification()));
        data.put("note", vegeInfo.getNote());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        data.put("updateTime", sdf.format(vegeInfo.getUpdateTime()));
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_vegeinfo.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryById(HttpServletRequest request) {
        String vegeid = request.getParameter("vegeId");
        VegeInfo vege = vegeService.queryById(vegeid);
        JSONObject data = new JSONObject();
        data.put("vegeId", vege.getVegeId());
        data.put("vegeName", vege.getVegeName());
        data.put("alias", vege.getAlias());
        String imgPath = "";
        imgPath = imageService.queryPathByUuid(vege.getImgUuid());
        data.put("imgPath", "http://8.142.64.137:8080/show_img?imgPath="+imgPath);
        data.put("introduction", vege.getIntroduction());
        data.put("classification", Constants.VEGE_CLASS_MAP.get(vege.getClassification()));
        data.put("note", vege.getNote());
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        data.put("updateTime", sdf.format(vege.getUpdateTime()));
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_variety.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryVarieties(HttpServletRequest request, HttpServletResponse response) {
        String vegeid = request.getParameter("vegeId");
        VegeInfo vege = vegeService.queryById(vegeid);
        List<Variety> varietyList = vege.getVarieties();
        for (int index=0;index<varietyList.size();index++){
            varietyList.get(index).setVegeInfo(null);
        }
        JSONObject data = new JSONObject();
        data.put("varietyList",varietyList);
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_disease.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryDiseases(HttpServletRequest request, HttpServletResponse response) {
        String vegeid = request.getParameter("vegeId");
        VegeInfo vege = vegeService.queryById(vegeid);
        List<Disease> diseasesList = vege.getDiseases();
        List<MobileDisease> mobileDiseaseList = new ArrayList<>();
        for (int index=0;index<diseasesList.size();index++){
            diseasesList.get(index).setVegeInfo(null);
            MobileDisease mobileDisease = new MobileDisease();
            mobileDisease.setDisease(diseasesList.get(index));

            List<Symptom> symptomList = diseasesList.get(index).getSymptoms();
            List<Cure> cureList = diseasesList.get(index).getCures();
            mobileDisease.setCureList(cureList);
            mobileDisease.setSymptomList(symptomList);
            mobileDiseaseList.add(mobileDisease);
        }
        JSONObject data = new JSONObject();
        data.put("diseasesList",mobileDiseaseList);
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_breedstage.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryBreedstages(HttpServletRequest request, HttpServletResponse response) {
        String vegeid = request.getParameter("vegeId");
        VegeInfo vege = vegeService.queryById(vegeid);
        List<BreedStage> breedStageList = vege.getBreedStages();
        for (int index=0;index<breedStageList.size();index++){
            breedStageList.get(index).setVegeInfo(null);
        }
        JSONObject data = new JSONObject();
        data.put("breedStageList",breedStageList);
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_company.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryCompany(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> condition = new HashMap<>();
        condition.put("page","1");
        condition.put("size","8");
        Page<Company> companyPage = companyService.query(condition);
        List<Company> companyList = companyPage.getContent();
        JSONObject data = new JSONObject();
        data.put("companyList",companyList);
        return buildResponse(data);
    }

    @RequestMapping(value = "/mobile_standard.json", method = {RequestMethod.GET, RequestMethod.GET})
    public Map queryStandard(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> condition = new HashMap<>();
        condition.put("page","1");
        condition.put("size","8");
        Page<Standard> standardPage = standardService.query(condition);
        List<Standard> standardList = standardPage.getContent();
        JSONObject data = new JSONObject();
        data.put("standardList", standardList);
        return buildResponse(data);
    }
}