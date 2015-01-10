package dao;

import model.Question;

public interface QuestionDAO {
	public Integer addQuestion(Question q);
	public Integer modifyQuestion(Question q);
	public Integer deleteQuestion(Integer qid);
	public Integer getNewQuestionOrderByQnid(Integer qnid);
}
