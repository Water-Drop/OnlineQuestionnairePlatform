package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.User;
import model.UserLogin;
import util.JDBCHelper;
import dao.UserDAO;


public class UserDAOimpl implements UserDAO{
	JDBCHelper jh = new JDBCHelper();
	public User getUserByUsernamePassword(String username, String password){
		User u = new User();
		Connection conn = null;
		try {
			conn = jh.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT id,email,type FROM oqp.user WHERE username=? and password=? and status=0");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			rs.last();
			int count = rs.getRow();
			if (count == 0){
				return null;
			} else {
				rs.first();
				u.setId(rs.getInt("id"));
				u.setEmail(rs.getString("email"));
				u.setType(rs.getInt("type"));
				u.setUsername(username);
				u.setPassword(password);
			}	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return u;
	}
	public Integer isExistUser(String username){//0: not exist 1: exist
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id FROM oqp.user WHERE username=? and status=0");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.first() != false){
				rtn = 1;
			} else {
				rtn = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	public Integer getUidByUsernamePasswordType(String username, String password, Integer type){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer userid = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id FROM oqp.user WHERE username=? and password=? and type=? and status=0");
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setInt(3, type);
			rs = ps.executeQuery();
			if (rs.first() != false){
				userid = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userid;
	}
	public Integer addUser(String username, String password, String email, Integer type){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer userid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.user(username, password, email, type, status) VALUES(?,?,?,?,0)");
			ips.setString(1, username);
			ips.setString(2, password);
			ips.setString(3, email);
			ips.setInt(4, type);
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				userid = Integer.parseInt(rs.getBigDecimal(1).toString());
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return userid;
	}
	
	public Integer addAuthToken(Integer uid, String createTime, String authToken){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.userlogin(uid, createtime, authtoken, status) VALUES(?,?,?,0)");
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.setInt(1, uid);
			ips.setString(2, createTime);
			ips.setString(3, authToken);
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				rtn = Integer.parseInt(rs.getBigDecimal(1).toString());
			}
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	
	public List<UserLogin> getAuthTokensByUid(Integer uid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<UserLogin> uls = new ArrayList<UserLogin>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,createtime,authtoken FROM oqp.userlogin WHERE uid=? AND status=0");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			while (rs.next()) {
				UserLogin ul = new UserLogin();
				ul.setId(rs.getInt("id"));
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				ul.setCreateTime(fmt.parse(rs.getString("createtime")));
				ul.setAuthToken(rs.getString("authtoken"));
				ul.setUid(uid);
				ul.setStatus(0);
				uls.add(ul);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return uls;
	}
	
	public Integer deleteAuthToken(Integer uid, String authToken){
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.userlogin SET status=1 WHERE uid=? AND authToken=? AND status=0");
			ps.setInt(1, uid);
			ps.setString(2, authToken);
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public String getUsernameByUid(Integer uid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String rtn = "";
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT username FROM oqp.user WHERE id=? and status=0");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			if (rs.first() != false){
				rtn = rs.getString("username");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
}
