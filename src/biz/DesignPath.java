package biz;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import util.MD5Helper;
import model.Design;
import net.sf.json.JSONObject;
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
}
