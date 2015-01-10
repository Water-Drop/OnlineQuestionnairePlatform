package biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONObject;
import util.JDBCHelper;
import util.MD5Helper;
import dao.UserDAO;
import dao.impl.UserDAOimpl;

@Path("/account")
public class Account{
	@Context HttpServletRequest req; 
	JDBCHelper jh = new JDBCHelper();
	UserDAO ud = new UserDAOimpl();
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(String param){
		JSONObject json_param = JSONObject.fromObject(param);
		String loginname = json_param.getString("loginname");
		String password = json_param.getString("password");
		Integer type = json_param.getInt("type");
		Integer userid = -1;
		Integer status = -1;//0: Success 1:miss information 2:wrong loginname or password
		String authToken = "";
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (loginname == null || password == null || type == null){
			status = 1;
		} else {
			userid = ud.getUidByUsernamePasswordType(loginname, password, type);
			if (userid > 0){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				String createTime = fmt.format(new Date()).toString();
				authToken = MD5Helper.generateMD5(createTime + requestIp + userAgent);
				if (ud.addAuthToken(userid, createTime, authToken) > 0){
					status = 0;
				}
			} else {
				status = 2;
			}	
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("userid", userid.toString());
		map.put("authToken", authToken);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	public int logout(int userid){
		int rtn = -1;
		return rtn;
	}
	@Path("/register")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String register(String param){
		JSONObject json_param = JSONObject.fromObject(param);
		String username = json_param.getString("username");
		String password = json_param.getString("password");
		String email = json_param.getString("email");
		Integer type = json_param.getInt("type");
		Integer userid = -1;
		Integer status = -1; //0: Success 1:miss information
		String authToken = "";
		String requestIp = req.getRemoteAddr();
		String userAgent = req.getHeader("user-agent");
		if (username == null || password == null || email == null || type == null){
			status = 1;
		} else {
			userid = ud.addUser(username, password, email, type);
			if (userid > 0){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				String createTime = fmt.format(new Date()).toString();
				authToken = MD5Helper.generateMD5(createTime + requestIp + userAgent);
				if (ud.addAuthToken(userid, createTime, authToken) > 0){
					status = 0;
				}
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("userid", userid.toString());
		map.put("authToken", authToken);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	public int suspend(int userid){
		int rtn = -1;
		return rtn;
	}
	public int resume(int userid){
		int rtn = -1;
		return rtn;
	}
}
