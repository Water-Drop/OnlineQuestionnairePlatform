package dao;

import java.util.List;

import model.User;
import model.UserLogin;

public interface UserDAO {
	public User getUserByUsernamePassword(String username, String password);
	public Integer isExistUser(String username);
	public Integer getUidByUsernamePasswordType(String username, String password, Integer type);
	public Integer addUser(String username, String password, String email, Integer type);
	public Integer addAuthToken(Integer uid, String createTime, String authToken);
	public List<UserLogin> getAuthTokensByUid(Integer uid);
	public Integer deleteAuthToken(Integer uid, String authToken);
}
