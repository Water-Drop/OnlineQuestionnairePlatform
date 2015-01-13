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

@Path("/design")
public class QNDesign {
	@Context HttpServletRequest req; 
	QuestionDAO qd = new QuestionDAOimpl();
	QuestionnaireDAO qnd = new QuestionnaireDAOimpl();
	OptionDAO od = new OptionDAOimpl();
	RecordDAO rd = new RecordDAOimpl();
	Auth au = new Auth();
	@Path("/addqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addQuestionnaire(String param){
		Integer status = -1;
		Integer qnid = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			Questionnaire qn = new Questionnaire();
			qn.setUid(uid);
			qn.setTitle(paramjson.getString("title"));
			qn.setDescription(paramjson.getString("description"));
			qn.setCreateTime(new Date());
			qn.setType(paramjson.getInt("type"));
			qnid = qnd.addQuestionnaire(qn);
			if (qnid > 0){
				status = 0;
			}
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("qnid", qnid.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/getqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getQuestionnairesByUid(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Questionnaire> qns = new ArrayList<Questionnaire>();
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			qns = qnd.getQuestionnairesByUid(uid);
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
						qn_map.put("fillcount", rd.getFillCountByQnid(qns.get(i).getId()).toString());
					}
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
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
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
		} else {
			status = -2;//request denied!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/modifyqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String modifyQuestionnaire(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			Questionnaire qn = new Questionnaire();
			qn.setUid(uid);
			qn.setId(paramjson.getInt("qnid"));;
			qn.setTitle(paramjson.getString("title"));
			qn.setDescription(paramjson.getString("description"));
			qn.setType(paramjson.getInt("type"));
			status = qnd.modifyQuestionnaireInfo(qn);
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/releaseqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String releaseQuestionnaire(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			status = qnd.releaseQuestionnaire(paramjson.getInt("qnid"));
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/deleteqn")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteQuestionnaire(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			status = qnd.deleteQuestionnaire(paramjson.getInt("qnid"));
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/addq")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String addQuestion(String param){
		Integer status = -1;
		Integer qid = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			Question q = new Question();
			q.setQnid(paramjson.getInt("qnid"));
			q.setTitle(paramjson.getString("title"));
			q.setDescription(paramjson.getString("description"));
			q.setCreateTime(new Date());
			q.setOrder(paramjson.getDouble("order"));
			q.setType(paramjson.getInt("type"));
			qid = qd.addQuestion(q);
			if (qid > 0){
				status = 0;
			}
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("qid", qid.toString());
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
		} else {
			status = -2;//request denied!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/modifyq")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String modifyQuestion(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		Integer qid = paramjson.getInt("qid");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			Question q = new Question();
			q.setId(qid);
			q.setTitle(paramjson.getString("title"));
			q.setDescription(paramjson.getString("description"));
			q.setOrder(paramjson.getDouble("order"));
			status = qd.modifyQuestion(q);
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("qid", qid.toString());
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/deleteq")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteQuestion(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			status = qd.deleteQuestion(paramjson.getInt("qid"));
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/modifyos")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String modifyOptions(String param){
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			JSONArray osjson = paramjson.getJSONArray("options");
			if (od.deleteAllOptions(paramjson.getInt("qid")) != -1){
				for (int i = 0; i < osjson.size(); i++){
					JSONObject ojson = JSONObject.fromObject(osjson.get(i));
					Option o = new Option();
					o.setQid(paramjson.getInt("qid"));
					o.setContent(ojson.getString("content"));
					o.setOrder(ojson.getInt("order"));
					if (od.addOption(o) < 0){
						break;
					} else{
						continue;
					}
				}
			}	
		}
		return getOptionsByQid(param);
	}
	public String getOptionsByQid(String param){
		Integer status = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Option> os = new ArrayList<Option>();
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			os = od.getOptionsByQid(paramjson.getInt("qid"));
			if (os.size() > 0){
				status = 0;
				List<String> o_jsons = new ArrayList<String>();
				for (int i = 0; i < os.size(); i++){
					Map<String, String> o_map = new HashMap<String, String>();
					o_map.put("id", os.get(i).getId().toString());
					o_map.put("qid", os.get(i).getQid().toString());
					o_map.put("content", os.get(i).getContent());
					o_map.put("order", os.get(i).getOrder().toString());
					o_map.put("status", os.get(i).getStatus().toString());
					JSONObject o_json = JSONObject.fromObject(o_map);
					o_jsons.add(o_json.toString());
				}
				JSONArray jsonArray = JSONArray.fromObject(o_jsons);
				map.put("Options", jsonArray.toString());
			} else {
				status = 1;// no such option!
			}
		} else {
			status = -2;//request denied!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
}
