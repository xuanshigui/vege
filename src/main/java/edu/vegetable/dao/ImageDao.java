package edu.vegetable.dao;

import edu.vegetable.model.Image;

import java.util.List;
import java.util.Map;

public interface ImageDao {

    public boolean add(Image data);

    public boolean delete(String imgId);

    public boolean update(Image data);

    public Image queryById(String imgId);

    public Image queryByUuid(String imgUuid);

    public List<Image> query(Map<String, String> condition);

    public int queryTotal(Map<String, String> condition);
}
