package edu.vegetable.controller;

import com.alibaba.fastjson.JSONObject;
import edu.vegetable.model.CultivateMode;
import edu.vegetable.model.Image;
import edu.vegetable.model.Variety;
import edu.vegetable.model.VegeInfo;
import edu.vegetable.service.CultivateModeService;
import edu.vegetable.service.ImageService;
import edu.vegetable.service.VarietyService;
import edu.vegetable.service.VegeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class VarietyController extends BaseController {

    private final VarietyService varietyService;
    private final VegeInfoService vegeInfoService;
    private final ImageService imageService;
    private final CultivateModeService cultivateModeService;

    @Autowired
    public VarietyController(VarietyService varietyService, VegeInfoService vegeInfoService,ImageService imageService,CultivateModeService cultivateModeService) {
        this.varietyService = varietyService;
        this.vegeInfoService = vegeInfoService;
        this.imageService = imageService;
        this.cultivateModeService = cultivateModeService;
    }

    @RequestMapping(value = "/add_variety.json", method = RequestMethod.POST)
    public Map add(HttpServletRequest request, HttpServletResponse response) {

        List<String> fields = Arrays.asList( "varietyName", "varietyImg", "introduction", "vegeId", "area", "source", "cultivateModes");
        Map<String, String> data = buildData(request,fields);
        Variety variety = new Variety();
        variety.setVarietyName(data.get("varietyName"));
        VegeInfo vegeInfo = vegeInfoService.queryById(data.get("vegeId"));
        variety.setVegeInfo(vegeInfo);

        //??????????????????
        if(data.get("cultivateModes")!=null){
            String[] cm = data.get("cultivateModes").split(",");
            List<CultivateMode> cultivateModeList = new ArrayList<>();
            for(String cmId:cm){
                cultivateModeList.add(cultivateModeService.queryById(cmId));
            }
            variety.setCultivateModes(cultivateModeList);
        }
        //????????????
        if (data.get("varietyImg")!=null&&!data.get("varietyImg").equals("")){
            String imgUuid = imageService.add(data.get("varietyImg"),Variety.class.getSimpleName());
            variety.setImgUuid(imgUuid);
        }
        variety.setDescription(data.get("introduction"));
        variety.setArea(data.get("area"));
        variety.setSource(data.get("source"));
        variety.setTimestamp(new Timestamp(System.currentTimeMillis()));
        boolean flag = varietyService.add(variety);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/delete_variety.json", method = RequestMethod.GET)
    public Map delete(HttpServletRequest request, HttpServletResponse response) {
        String varietyId = request.getParameter("varietyId");
        boolean flag = varietyService.delete(varietyId);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/update_variety.json", method = RequestMethod.POST)
    public Map update(HttpServletRequest request, HttpServletResponse response) {
        List<String> fields = Arrays.asList("varietyId", "varietyName", "varietyImg", "imguuid", "introduction", "vegeId", "area", "source", "cultivateModes");
        Map<String, String> data = buildData(request,fields);
        Variety variety = varietyService.queryById(data.get("varietyId"));
        //??????VegeInfo???Variety??????
        if(Integer.parseInt(data.get("vegeId"))!=variety.getVegeInfo().getVegeId()){
            //????????????VegeInfo
            VegeInfo vegeInfo = variety.getVegeInfo();
            vegeInfo.getVarieties().remove(variety);
            //????????????VegeInfo
            VegeInfo newVegeInfo = vegeInfoService.queryById(data.get("vegeId"));
            newVegeInfo.getVarieties().add(variety);
            vegeInfoService.update(vegeInfo);
            variety.setVegeInfo(newVegeInfo);
        }

        variety.setVarietyName(data.get("varietyName"));
        if (data.get("imguuid")==null||data.get("imguuid").equals("")){
            //????????????????????????
            Image oldImg = imageService.queryByUuid(variety.getImgUuid());
            if(oldImg!=null){
                imageService.delete(String.valueOf(oldImg.getImgId()));
            }
            String imgUuid = imageService.add(data.get("varietyImg"),variety.getClass().getSimpleName());
            variety.setImgUuid(imgUuid);
        }
        //??????????????????
        String[] cm = data.get("cultivateModes").split(",");
        List<CultivateMode> cultivateModeList = new ArrayList<>();
        for(String cmId:cm){
            cultivateModeList.add(cultivateModeService.queryById(cmId));
        }
        variety.setCultivateModes(cultivateModeList);
        variety.setDescription(data.get("introduction"));
        variety.setArea(data.get("area"));
        variety.setSource(data.get("source"));
        variety.setTimestamp(new Timestamp(System.currentTimeMillis()));
        boolean flag = varietyService.update(variety);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/query_variety.json", method = {RequestMethod.POST, RequestMethod.GET})
    public Map query(HttpServletRequest request, HttpServletResponse response) {
        List<String> fields = Arrays.asList("varietyName","vegeName", "page", "size");
        Map<String, String> condition = buildData(request, fields);
        Page<Variety> result = varietyService.query(condition);
        JSONObject data = new JSONObject();
        data.put("total", result.getTotalElements());
        data.put("rows", result.getContent());
        return buildResponse(data);
    }

    @RequestMapping(value = "/query_varietybyid.json", method = {RequestMethod.POST, RequestMethod.GET})
    public Map queryById(HttpServletRequest request, HttpServletResponse response) {
        String varietyid = request.getParameter("varietyId");

        Variety variety = varietyService.queryById(varietyid);
        JSONObject data = new JSONObject();
        data.put("varietyId", variety.getVarietyId());
        data.put("vegeName", variety.getVegeInfo().getVegeName());
        data.put("varietyName", variety.getVarietyName());
        data.put("cultivatemodes",variety.getCultivateModes());
        data.put("description", variety.getDescription());
        data.put("area", variety.getArea());
        data.put("timestamp", variety.getTimestamp());
        String imgPath = "";
        imgPath = imageService.queryPathByUuid(variety.getImgUuid());
        data.put("imgPath", URL+imgPath);
        data.put("imgUuid", variety.getImgUuid());
        data.put("source", variety.getSource());

        //??????????????????
        Map<String,String> vegeNameMap = vegeInfoService.getVegeIdAndName();
        data.put("vegeNameMap",vegeNameMap);
        Map<String,String> cultivateModes = cultivateModeService.getCultivateModeName();
        data.put("modeNameMap",cultivateModes);
        return buildResponse(data);
    }

    @RequestMapping(value = "/load_vegename.json", method = {RequestMethod.GET})
    public Map getVegeNames(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> vegeNames = vegeInfoService.getVegeIdAndName();
        Map<String,String> cultivateModes = cultivateModeService.getCultivateModeName();
        //List<CultivateMode> cultivateModeList = cultivateModeService.findAll();
        JSONObject data = new JSONObject();
        data.put("vegeNameMap",vegeNames);
        data.put("modeNameMap",cultivateModes);
        //data.put("cultivateModeList",cultivateModeList);
        return buildResponse(data);
    }
}
