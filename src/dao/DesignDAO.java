package dao;

import model.Design;

public interface DesignDAO {
	String getDatapathByUid(Integer uid);
	Integer addDatapath(Design d);
}
