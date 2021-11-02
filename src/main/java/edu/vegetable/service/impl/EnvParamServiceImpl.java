package edu.vegetable.service.impl;

import edu.vegetable.dao.BreedStageRepository;
import edu.vegetable.dao.EnvParamRepository;
import edu.vegetable.model.BreedStage;
import edu.vegetable.model.EnvParam;
import edu.vegetable.service.EnvParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
@Service
public class EnvParamServiceImpl extends BaseService implements EnvParamService {

    @Autowired
    EnvParamRepository envParamRepository;

    @Autowired
    BreedStageRepository breedStageRepository;

    @Override
    @Transactional
    public boolean add(EnvParam envParam) {

        try {
            envParamRepository.saveAndFlush(envParam);
            BreedStage breedStage = envParam.getBreedStage();
            breedStage.getEnvParams().add(envParam);
            breedStageRepository.save(breedStage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Transactional
    @Override
    public boolean delete(String epId) {
        try {
            EnvParam envParam = envParamRepository.findByEpId(Integer.parseInt(epId));
            BreedStage breedStage = envParam.getBreedStage();
            breedStage.getEnvParams().remove(envParam);
            breedStageRepository.save(breedStage);
            envParamRepository.delete(envParam);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean update(EnvParam envParam) {

        try {
            envParamRepository.saveAndFlush(envParam);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Page<EnvParam> query(Map<String, String> condition) {
        String paramNameStr = condition.get("paramName");
        String stageNameStr = condition.get("stageName");
        Pageable pageable = getPageable(condition);
        if (paramNameStr != null && !paramNameStr.equals("")) {
            paramNameStr = "%"+paramNameStr+"%";
            if (stageNameStr != null && !stageNameStr.equals("")) {
                stageNameStr = "%" + stageNameStr + "%";
                return envParamRepository.findAllByParaNameLikeAndBreedStage_StageNameLike(paramNameStr,stageNameStr,pageable);
            }
            return envParamRepository.findAllByParaNameLike(paramNameStr,pageable);
        }
        if(stageNameStr != null && !stageNameStr.equals("")){
            stageNameStr = "%" + stageNameStr + "%";
            return envParamRepository.findAllByBreedStage_StageNameLike(stageNameStr,pageable);
        }
        return envParamRepository.findAll(pageable);
    }

    @Override
    public long queryTotal(Map<String, String> condition) {
        return envParamRepository.count();
    }

    @Override
    public EnvParam queryById(String epId) {
        return envParamRepository.findByEpId(Integer.parseInt(epId));
    }
}
