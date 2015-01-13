package biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import model.AnswerRecord;
import model.FillRecord;
import model.Option;
import model.Question;
import model.Questionnaire;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import dao.OptionDAO;
import dao.QuestionDAO;
import dao.QuestionnaireDAO;
import dao.RecordDAO;
import dao.impl.OptionDAOimpl;
import dao.impl.QuestionDAOimpl;
import dao.impl.QuestionnaireDAOimpl;
import dao.impl.RecordDAOimpl;

@Path("/fill")
public class QNFill {
	@Context HttpServletRequest req; 
	QuestionDAO qd = new QuestionDAOimpl();
	QuestionnaireDAO qnd = new QuestionnaireDAOimpl();
	OptionDAO od = new OptionDAOimpl();
	Auth au = new Auth();
	RecordDAO rd = new RecordDAOimpl();
	@Path("/getpublicqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getPublicQuestionnaires(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Questionnaire> qns = new ArrayList<Questionnaire>();
		qns = qnd.getPublicQuestionnaires();
		if (qns.size() > 0){
			status = 0;
			List<String> qn_jsons = new ArrayList<String>();
			for (int i = 0; i < qns.size(); i++){
				Map<String, String> qn_map = new HashMap<String, String>();
				qn_map.put("id", qns.get(i).getId().toString());
				qn_map.put("uid", qns.get(i).getUid().toString());
				qn_map.put("title", qns.get(i).getTitle());
				qn_map.put("description", qns.get(i).getDescription());
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				qn_map.put("createTime", fmt.format(qns.get(i).getCreateTime()));
				qn_map.put("type", qns.get(i).getType().toString());
				qn_map.put("status", qns.get(i).getStatus().toString());
				if (qns.get(i).getStatus() == 1){
					qn_map.put("path", qns.get(i).getPath());
					qn_map.put("releaseTime", fmt.format(qns.get(i).getReleaseTime()));
				}
				JSONObject qn_json = JSONObject.fromObject(qn_map);
				qn_jsons.add(qn_json.toString());
			}
			JSONArray jsonArray = JSONArray.fromObject(qn_jsons);
			map.put("Questionnaires", jsonArray.toString());
		} else {
			status = 1;// no such questionnaire!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/getallreleaseqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllReleaseQuestionnaires(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Questionnaire> qns = new ArrayList<Questionnaire>();
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			qns = qnd.getAllReleaseQuestionnaires();
			if (qns.size() > 0){
				status = 0;
				List<String> qn_jsons = new ArrayList<String>();
				for (int i = 0; i < qns.size(); i++){
					Map<String, String> qn_map = new HashMap<String, String>();
					qn_map.put("id", qns.get(i).getId().toString());
					qn_map.put("uid", qns.get(i).getUid().toString());
					qn_map.put("title", qns.get(i).getTitle());
					qn_map.put("description", qns.get(i).getDescription());
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					qn_map.put("createTime", fmt.format(qns.get(i).getCreateTime()));
					qn_map.put("type", qns.get(i).getType().toString());
					qn_map.put("status", qns.get(i).getStatus().toString());
					if (qns.get(i).getStatus() == 1){
						qn_map.put("path", qns.get(i).getPath());
						qn_map.put("releaseTime", fmt.format(qns.get(i).getReleaseTime()));
					}
					qn_map.put("fillStatus", rd.isHaveFillRecords(uid, qns.get(i).getId()).toString());// 0 not filled before 1 filled
					JSONObject qn_json = JSONObject.fromObject(qn_map);
					qn_jsons.add(qn_json.toString());
				}
				JSONArray jsonArray = JSONArray.fromObject(qn_jsons);
				map.put("Questionnaires", jsonArray.toString());
			} else {
				status = 1;// no such questionnaire!
			}
		} else {
			status = -2;//request denied!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/getqnbyid")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestionnaireById(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		Questionnaire qn = new Questionnaire();
		JSONObject paramjson = JSONObject.fromObject(param);
		qn = qnd.getQuestionnaireById(paramjson.getInt("qnid"));
		if (qn.getStatus() != 2){//2 means no qn for the id
			status = 0;
			map.put("qnid", qn.getId().toString());
			map.put("uid", qn.getUid().toString());
			map.put("title", qn.getTitle());
			map.put("description", qn.getDescription());
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			map.put("createTime", fmt.format(qn.getCreateTime()));
			map.put("type", qn.getType().toString());
			map.put("qnstatus", qn.getStatus().toString());
			if (qn.getStatus() == 1){
				map.put("path", qn.getPath());
				map.put("releaseTime", fmt.format(qn.getReleaseTime()));
				}
			} else {
			status = 1;// no such questionnaire!
			}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/getq")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestionsByQnid(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Question> qs = new ArrayList<Question>();
		JSONObject paramjson = JSONObject.fromObject(param);
		qs = qd.getQuestionsByQnid(paramjson.getInt("qnid"));
		if (qs.size() > 0){
			status = 0;
			List<String> q_jsons = new ArrayList<String>();
			for (int i = 0; i < qs.size(); i++){
				Map<String, String> q_map = new HashMap<String, String>();
				q_map.put("id", qs.get(i).getId().toString());
				q_map.put("qnid", qs.get(i).getQnid().toString());
				q_map.put("title", qs.get(i).getTitle());
				q_map.put("description", qs.get(i).getDescription());
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				q_map.put("creatTime", fmt.format(qs.get(i).getCreateTime()));
				q_map.put("order", qs.get(i).getOrder().toString());
				q_map.put("type", qs.get(i).getType().toString());
				if (qs.get(i).getType() == 1 || qs.get(i).getType() == 2){
					List<Option> os = od.getOptionsByQid(qs.get(i).getId());
					if (os.size() > 0){
						List<String> o_jsons = new ArrayList<String>();
						for (int j = 0; j < os.size(); j++){
							Map<String, String> o_map = new HashMap<String, String>();
							o_map.put("id", os.get(j).getId().toString());
							o_map.put("qid", os.get(j).getQid().toString());
							o_map.put("content", os.get(j).getContent());
							o_map.put("order", os.get(j).getOrder().toString());
							o_map.put("status", os.get(j).getStatus().toString());
							JSONObject o_json = JSONObject.fromObject(o_map);
							o_jsons.add(o_json.toString());
						}
						JSONArray ojsonArray = JSONArray.fromObject(o_jsons);
						q_map.put("Options", ojsonArray.toString());
					}	
				}
				q_map.put("status", qs.get(i).getStatus().toString());
				JSONObject q_json = JSONObject.fromObject(q_map);
				q_jsons.add(q_json.toString());
			}
			JSONArray jsonArray = JSONArray.fromObject(q_jsons);
			map.put("Questions", jsonArray.toString());
		} else {
			status = 1;// no such question!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/addr")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addRecords(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		JSONObject paramjson = JSONObject.fromObject(param);
		Integer uid = paramjson.getInt("uid");
		Integer fid = -1;
		if (uid != 6){
			String authToken = paramjson.getString("authToken");
			String requestIp = req.getRemoteAddr();
			String userAgent = req.getHeader("user-agent");
			if (au.authUser(uid, requestIp, userAgent, authToken) != 0){
				status = -2;
				map.put("status", status.toString());
				JSONObject json = JSONObject.fromObject(map);
				return json.toString();
				}
		}
		FillRecord fr = new FillRecord();
		fr.setQnid(paramjson.getInt("qnid"));
		fr.setUid(uid);
		fr.setFillTime(new Date());
		fid = rd.addFillRecord(fr);
		if (fid > 0){
			JSONArray arsjson = paramjson.getJSONArray("answers");
			for (int i = 0; i < arsjson.size(); i++){
				JSONObject arjson = JSONObject.fromObject(arsjson.get(i));
				if (arjson.getInt("qtype") == 1){//one choice
					AnswerRecord ar = new AnswerRecord();
					ar.setFid(fid);
					ar.setQid(arjson.getInt("qid"));
					ar.setQtype(arjson.getInt("qtype"));
					ar.setOid(arjson.getInt("oid"));
					rd.addAnswerRecord(ar);
				} else if (arjson.getInt("qtype") == 2){//multi choice
					JSONArray osjson = arjson.getJSONArray("options");
					for (int j = 0; j < osjson.size(); j++){
						JSONObject ojson = JSONObject.fromObject(osjson.get(j));
						AnswerRecord ar = new AnswerRecord();
						ar.setFid(fid);
						ar.setQid(arjson.getInt("qid"));
						ar.setQtype(arjson.getInt("qtype"));
						ar.setOid(ojson.getInt("oid"));
						rd.addAnswerRecord(ar);
					}
				} else {
					AnswerRecord ar = new AnswerRecord();
					ar.setFid(fid);
					ar.setQid(arjson.getInt("qid"));
					ar.setQtype(arjson.getInt("qtype"));
					ar.setContent(arjson.getString("content"));
					rd.addAnswerRecord(ar);
				}
				status = 0;
			}
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();	
	}
	@Path("/getresults")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestionnaireResults(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Question> qs = new ArrayList<Question>();
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			qs = qd.getQuestionsByQnid(paramjson.getInt("qnid"));
			if (qs.size() > 0){
				status = 0;
				List<String> q_jsons = new ArrayList<String>();
				for (int i = 0; i < qs.size(); i++){
					Map<String, String> q_map = new HashMap<String, String>();
					q_map.put("id", qs.get(i).getId().toString());
					q_map.put("qnid", qs.get(i).getQnid().toString());
					q_map.put("title", qs.get(i).getTitle());
					q_map.put("description", qs.get(i).getDescription());
					SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					q_map.put("creatTime", fmt.format(qs.get(i).getCreateTime()));
					q_map.put("order", qs.get(i).getOrder().toString());
					q_map.put("type", qs.get(i).getType().toString());
					if (qs.get(i).getType() == 1 || qs.get(i).getType() == 2){
						List<Option> os = od.getOptionsByQid(qs.get(i).getId());
						if (os.size() > 0){
							List<String> o_jsons = new ArrayList<String>();
							for (int j = 0; j < os.size(); j++){
								Map<String, String> o_map = new HashMap<String, String>();
								o_map.put("id", os.get(j).getId().toString());
								o_map.put("qid", os.get(j).getQid().toString());
								o_map.put("content", os.get(j).getContent());
								o_map.put("order", os.get(j).getOrder().toString());
								o_map.put("count", rd.getFillCountByOid(os.get(j).getId()).toString());
								o_map.put("status", os.get(j).getStatus().toString());
								JSONObject o_json = JSONObject.fromObject(o_map);
								o_jsons.add(o_json.toString());
							}
							JSONArray ojsonArray = JSONArray.fromObject(o_jsons);
							q_map.put("Options", ojsonArray.toString());
						}	
					} else {
						List<AnswerRecord> ars = rd.getAnswerRecordsByQid(qs.get(i).getId());
						if (ars.size() > 0){
							List<String> ar_jsons = new ArrayList<String>();
							for (int j = 0; j < ars.size(); j++){
								Map<String, String> ar_map = new HashMap<String, String>();
								ar_map.put("content", ars.get(i).getContent());
								
								JSONObject ar_json = JSONObject.fromObject(ar_map);
								ar_jsons.add(ar_json.toString());
							}
							JSONArray arjsonArray = JSONArray.fromObject(ar_jsons);
							q_map.put("Answers", arjsonArray.toString());
						}
					}
					q_map.put("status", qs.get(i).getStatus().toString());
					JSONObject q_json = JSONObject.fromObject(q_map);
					q_jsons.add(q_json.toString());
				}
				JSONArray jsonArray = JSONArray.fromObject(q_jsons);
				map.put("Questions", jsonArray.toString());
			} else {
				status = 1;// no such question!
			}
		} else {
			status = -2;//request denied!
		}
		map.put("fillcount", rd.getFillRecordsByQnid(paramjson.getInt("qnid")).toString());
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
}
