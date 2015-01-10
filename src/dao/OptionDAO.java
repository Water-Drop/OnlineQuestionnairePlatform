package dao;

import model.Option;

public interface OptionDAO {
	public Integer addOption(Option o);
	public Integer modifyOption(Option o);
	public Integer deleteOption(Integer oid);
}
