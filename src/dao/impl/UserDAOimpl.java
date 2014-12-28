package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.JDBCHelper;
import model.User;
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
	public Integer addUser(String username, String password, String email, String type){
		Integer userid = -1;
		return userid;
	}
}
