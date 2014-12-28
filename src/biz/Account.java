package biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import model.User;
import net.sf.json.JSONObject;
import util.JDBCHelper;
import dao.UserDAO;
import dao.impl.UserDAOimpl;

@Path("/account")
public class Account{
	JDBCHelper jh = new JDBCHelper();
	UserDAO ud = new UserDAOimpl();
	@Path("/login")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@QueryParam("loginname")String loginname, @QueryParam("password")String password){
		Integer userid = -1;
		Integer status = -1;//0: Success 1:miss information 2:wrong loginname or password
		if (loginname == null || password == null){
			status = 1;
		} else {
			User u = new User();
			u = ud.getUserByUsernamePassword(loginname, password);
			if (null != u){
				userid = u.getId();
				status = 0;
			} else {
				status = 2;
			}	
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("userid", userid.toString());
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
	public String register(@QueryParam("username")String username, @QueryParam("password")String password,
						   @QueryParam("email")String email, @QueryParam("phonenumber")String phonenumber){
		Integer userid = -1;
		Integer status = -1; //0: Success 1:miss information
		if (username == null || password == null || email == null ){
			status = 1;
		} else {
			Connection conn = null;
			try {
				conn = jh.getConnection();
				PreparedStatement ps1 = conn
						.prepareStatement("INSERT INTO ipin.user (username, email, phonenumber, password) VALUES (?, ?, ?, ?)");
				PreparedStatement ps2 = conn
						.prepareStatement("SELECT last_insert_id()");
				ps1.setString(1, username);
				ps1.setString(2, password);
				ps1.setString(3, email);
				ps1.setString(4, phonenumber);
				ps1.execute();
				ResultSet rs = ps2.executeQuery();
				rs.next();
				userid = rs.getInt(1);
				status = 0;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", status.toString());
		map.put("userid", userid.toString());
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
