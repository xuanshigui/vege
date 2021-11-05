package edu.vegetable.service;

import edu.vegetable.model.VegeInfo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface VegeInfoService {

    boolean add(VegeInfo data);

    boolean delete(String vegeId);

    boolean update(VegeInfo data);

    Page<VegeInfo> query(Map<String, String> condition);

    public Map<String, String> getVegeIdAndName();

    long queryTotal(Map<String, String> condition);

    VegeInfo queryById(String vegeId);

    VegeInfo queryByName(String vegeName);

    String getVegeNameById(String vegeId);
}
