package biz;

import java.text.SimpleDateFormat;
import java.util.List;

import util.MD5Helper;
import model.UserLogin;
import dao.UserDAO;
import dao.impl.UserDAOimpl;

public class Auth {
	UserDAO ud = new UserDAOimpl();
	public Integer authUser(Integer uid, String requestIp, String userAgent, String authToken){
		Integer rtn = -1;
		List<UserLogin> uls = ud.getAuthTokensByUid(uid);
		if (uls.size() == 0){
			rtn = 1;//No UserLogin authTokens, authentication false!
		} else {
			for (int i = 0; i < uls.size(); i ++){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				String createTime = fmt.format(uls.get(i).getCreateTime()).toString();
				String g_authToken = MD5Helper.generateMD5(createTime + requestIp + userAgent);
				if (g_authToken.equals(authToken) && authToken.equals(uls.get(i).getAuthToken())){
					rtn = 0;
					return rtn;
				} else {
					continue;
				}
			}
			rtn = 2;//No matched UserLogin authTokens, authentication false!
		}
		return rtn;
	}
}
