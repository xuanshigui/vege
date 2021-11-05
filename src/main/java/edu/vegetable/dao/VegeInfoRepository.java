package edu.vegetable.dao;

import edu.vegetable.model.VegeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VegeInfoRepository extends JpaRepository<VegeInfo, Integer> {


    VegeInfo findByVegeId(Integer vegeId);

    Page<VegeInfo> findAll(Pageable pageable);

    Page<VegeInfo> findAllByVegeNameLike(String vegeName, Pageable pageable);

    List<VegeInfo> findAllByVegeNameLike(String vegeName);

}
