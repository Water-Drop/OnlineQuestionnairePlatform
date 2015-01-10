package dao;

import java.util.List;

import model.Option;

public interface OptionDAO {
	public Integer addOption(Option o);
	public Integer modifyOption(Option o);
	public Integer deleteOption(Integer oid);
	public List<Option> getOptionsByQid(Integer qid);
	public Integer deleteAllOptions(Integer qid);
}
