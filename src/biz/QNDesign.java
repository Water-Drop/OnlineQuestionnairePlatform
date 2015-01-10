package biz;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import model.Question;
import model.Questionnaire;
import net.sf.json.JSONObject;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.impl.QuestionDAOimpl;
import dao.impl.QuestionnaireDAOimpl;

@Path("/design")
public class QNDesign {
	QuestionDAO qd = new QuestionDAOimpl();
	QuestionnaireDAO qnd = new QuestionnaireDAOimpl();
	public String addQuestionnaire(String param){
		Integer status = -1;
		Integer qnid = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		Questionnaire qn = new Questionnaire();
		qn.setUid(Integer.parseInt(paramjson.getString("uid")));
		qn.setTitle(paramjson.getString("title"));
		qn.setDescription(paramjson.getString("description"));
		qn.setCreateTime(new Date());
		qn.setType(paramjson.getInt("type"));
		qnid = qnd.addQuestionnaire(qn);
		if (qnid > 0){
			status = 0;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("qnid", qnid.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	public String addQuestion(String param){
		JSONObject paramjson = JSONObject.fromObject(param);
		Question q = new Question();
		q.setQnid(qnid);
		q.setTitle(paramjson.getString("title"));
		q.setDescription(paramjson.getString("description"));
		q.setType(paramjson.getInt("type"));
		Integer rtn = qd.addQuestion(q);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", rtn.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	public String modifyQuestion(Integer qnid, Integer qid, String param){
		JSONObject paramjson = JSONObject.fromObject(param);
		Question q = new Question();
		q.setQnid(qnid);
		q.setId(qid);
		q.setTitle(paramjson.getString("title"));
		q.setDescription(paramjson.getString("description"));
		q.setType(paramjson.getInt("type"));
		Integer rtn = qd.modifyQuestion(q);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", rtn.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	public String deleteQuestion(Integer qid){
		Integer rtn = qd.deleteQuestion(qid);
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", rtn.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

}
