package dao;

import java.util.List;

import model.Design;

public interface DesignDAO {
	public String getDatapathByUid(Integer uid);
	public Integer addDatapath(Design d);
	public List<Design> getAllDatapaths();	
}
