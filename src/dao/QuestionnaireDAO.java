package dao;

import java.util.List;

import model.Questionnaire;

public interface QuestionnaireDAO {
	public Integer addQuestionnaire(Questionnaire qn);
	public Integer modifyQuestionnaireInfo(Questionnaire qn);
	public Integer releaseQuestionnaire(Integer qnid);
	public Integer deleteQuestionnaire(Integer qnid);
	public List<Questionnaire> getQuestionnairesByUid(Integer uid);
}
