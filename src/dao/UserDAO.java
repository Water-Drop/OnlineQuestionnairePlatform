package dao;

import model.User;

public interface UserDAO {
	public User getUserByUsernamePassword(String username, String password);
	public Integer addUser(String username, String password, String email, String type);
}
