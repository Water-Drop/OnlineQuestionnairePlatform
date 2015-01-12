package dao;

import java.util.List;

import model.AnswerRecord;
import model.FillRecord;

public interface RecordDAO {
	public Integer addFillRecord(FillRecord fr);
	public List<FillRecord> getFillRecordsByUid(Integer uid);
	public List<FillRecord> getFillRecordsByQnid(Integer qnid);
	public Integer isHaveFillRecords(Integer uid, Integer qnid);
	public Integer addAnswerRecord(AnswerRecord ar);
	public List<AnswerRecord> getAnswerRecordsByFid(Integer fid);
	public List<AnswerRecord> getAnswerRecordsByQid(Integer qid);
	public Integer getFillCountByQnid(Integer qnid);
	public Integer getFillCountByOid(Integer oid);
}
