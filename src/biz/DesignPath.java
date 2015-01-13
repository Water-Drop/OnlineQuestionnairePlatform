package biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import model.Design;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.MD5Helper;
import dao.DesignDAO;
import dao.impl.DesignDAOimpl;

@Path("/designPath")
public class DesignPath {
	@Context HttpServletRequest req; 
	DesignDAO dd = new DesignDAOimpl();
	Auth au = new Auth();
	@Path("/get")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getDesignPath(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		String designPath = "";
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			designPath = dd.getDatapathByUid(uid);
			if (designPath.equals("")){
				Design d = new Design();
				d.setUid(uid);
				d.setDatapath(MD5Helper.generateMD5(uid+requestIp));
				if (dd.addDatapath(d) > 0){
					designPath = d.getDatapath();
					status = 0;
				} else {
					status = 1;
				}
			} else {
				status = 0;
			}
		} else {
			status = -2;//request denied!
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("designPath", designPath);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	@Path("/getall")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDesignPaths(String param){
		Integer status = -1;
		JSONObject paramjson = JSONObject.fromObject(param);
		String authToken = paramjson.getString("authToken");
		Integer uid = paramjson.getInt("uid");
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		Map<String, String> map = new HashMap<String, String>();
		List<Design> ds = new ArrayList<Design>();
		if (au.authUser(uid, requestIp, userAgent, authToken) == 0){
			ds = dd.getAllDatapaths();
			if (ds.size() > 0){
				status = 0;
				List<String> d_jsons = new ArrayList<String>();
				for (int i = 0; i < ds.size(); i++){
					Map<String, String> d_map = new HashMap<String, String>();
					d_map.put("id", ds.get(i).getId().toString());
					d_map.put("uid", ds.get(i).getUid().toString());
					d_map.put("designPath", ds.get(i).getDatapath());
					d_map.put("status", ds.get(i).getStatus().toString());
					JSONObject d_json = JSONObject.fromObject(d_map);
					d_jsons.add(d_json.toString());
				}
				JSONArray jsonArray = JSONArray.fromObject(d_jsons);
				map.put("Paths", jsonArray.toString());
			} else {
				status = 0;
			}
		} else {
			status = -2;//request denied!
		}
		map.put("status", status.toString());
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
}
