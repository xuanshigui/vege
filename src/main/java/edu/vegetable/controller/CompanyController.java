package edu.vegetable.controller;

import com.alibaba.fastjson.JSONObject;
import edu.vegetable.constants.Constants;
import edu.vegetable.model.Company;
import edu.vegetable.model.Image;
import edu.vegetable.service.CompanyService;
import edu.vegetable.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class CompanyController extends BaseController {

    private final CompanyService companyService;
    private final ImageService imageService;

    @Autowired
    public CompanyController(CompanyService companyService, ImageService imageService) {
        this.companyService = companyService;
        this.imageService = imageService;
    }

    @RequestMapping(value = "/add_company.json", method = RequestMethod.POST)
    public Map add(HttpServletRequest request, HttpServletResponse response) {

        List<String> fields = Arrays.asList( "companyName", "companyImg", "type", "imguuid", "introduction", "location", "contact", "telephone", "website");
        Map<String, String> data = buildData(request,fields);
        Company company = new Company();
        company.setCompanyName(data.get("companyName"));
        //存储图片
        String imgUuid = imageService.add(data.get("companyImg"),Company.class.getSimpleName());
        company.setImgUuid(imgUuid);
        company.setIntroduction(data.get("introduction"));
        company.setType(data.get("type"));
        company.setLocation(data.get("location"));
        company.setContact(data.get("contact"));
        company.setTelephone(data.get("telephone"));
        company.setWebsite(data.get("website"));
        boolean flag = companyService.add(company);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/delete_company.json", method = RequestMethod.GET)
    public Map delete(HttpServletRequest request, HttpServletResponse response) {
        String companyId = request.getParameter("companyId");
        boolean flag = companyService.delete(companyId);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/update_company.json", method = RequestMethod.POST)
    public Map update(HttpServletRequest request, HttpServletResponse response) {
        List<String> fields = Arrays.asList("companyId", "companyName", "companyImg", "imguuid", "introduction", "location","type", "contact", "telephone", "website");
        Map<String, String> data = buildData(request,fields);
        Company company = companyService.queryById(data.get("companyId"));
        company.setCompanyName(data.get("companyName"));
        //存储图片
        if (data.get("imguuid")==null||data.get("imguuid").equals("")){
            //建立图片
            Image oldImg = imageService.queryByUuid(company.getImgUuid());
            if(oldImg!=null){
                imageService.delete(String.valueOf(oldImg.getImgId()));
            }
            String imgUuid = imageService.add(data.get("companyImg"),company.getClass().getSimpleName());
            company.setImgUuid(imgUuid);
        }
        company.setIntroduction(data.get("introduction"));
        company.setType(data.get("type"));
        company.setLocation(data.get("location"));
        company.setContact(data.get("contact"));
        company.setTelephone(data.get("telephone"));
        company.setWebsite(data.get("website"));
        boolean flag = companyService.update(company);
        return buildResponse(flag);
    }

    @RequestMapping(value = "/query_company.json", method = {RequestMethod.POST, RequestMethod.GET})
    public Map query(HttpServletRequest request, HttpServletResponse response) {
        List<String> fields = Arrays.asList("companyName", "page", "size");
        Map<String, String> condition = buildData(request, fields);
        Page<Company> result = companyService.query(condition);
        long total = companyService.queryTotal(condition);
        JSONObject data = new JSONObject();
        data.put("total", result.getTotalElements());
        data.put("rows", result.getContent());
        return buildResponse(data);
    }

    @RequestMapping(value = "/query_companybyid.json", method = {RequestMethod.POST, RequestMethod.GET})
    public Map queryById(HttpServletRequest request, HttpServletResponse response) {
        String companyid = request.getParameter("companyId");

        Company company = companyService.queryById(companyid);
        JSONObject data = new JSONObject();
        data.put("companyId", company.getCompanyId());
        data.put("companyName", company.getCompanyName());
        //载入图片
        String imgPath = "";
        imgPath = imageService.queryPathByUuid(company.getImgUuid());
        data.put("imgPath", URL+imgPath);
        data.put("imgUuid", company.getImgUuid());
        data.put("introduction", company.getIntroduction());
        data.put("type", company.getType());
        data.put("location", company.getLocation());
        data.put("contact",company.getContact());
        data.put("telephone", company.getTelephone());
        data.put("website", company.getWebsite());

        //载入列表选项
        data.put("companyTypeMap",Constants.COMPANY_TYPE_MAP);

        return buildResponse(data);
    }

    @RequestMapping(value = "/load_companytype.json", method = {RequestMethod.GET})
    public Map getVegeNames(HttpServletRequest request, HttpServletResponse response) {

        JSONObject data = new JSONObject();
        data.put("companyTypeMap",Constants.COMPANY_TYPE_MAP);
        return buildResponse(data);
    }
}
