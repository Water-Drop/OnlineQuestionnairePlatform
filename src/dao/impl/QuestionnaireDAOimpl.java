package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Questionnaire;
import util.JDBCHelper;
import dao.QuestionnaireDAO;

public class QuestionnaireDAOimpl implements QuestionnaireDAO{
	JDBCHelper jh = new JDBCHelper();
	public Integer addQuestionnaire(Questionnaire qn){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer qnid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.questionnaire (uid,title,description,createtime,type,status) VALUES (?,?,?,?,?,0)");
			ips.setInt(1,qn.getUid());
			ips.setString(2,qn.getTitle());
			ips.setString(3,qn.getDescription());
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			ips.setString(4, fmt.format(qn.getCreateTime()));
			ips.setInt(5,qn.getType());
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				qnid = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return qnid;
	}
	public Integer modifyQuestionnaireInfo(Questionnaire qn){
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.questionnaire SET title=? AND description=? AND type=? WHERE id=?");
			ps.setString(1,qn.getTitle());
			ps.setString(2,qn.getDescription());
			ps.setInt(3,qn.getType());
			ps.setInt(4, qn.getId());
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public Integer releaseQuestionnaire(Integer qnid){//set status=1
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.questionnaire SET releasetime=? AND status=1 WHERE id=?");
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			ps.setString(1, fmt.format(new Date()));
			ps.setInt(2, qnid);
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public Integer deleteQuestionnaire(Integer qnid){//set status=2
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.questionnaire SET status=2 WHERE id=?");
			ps.setInt(1, qnid);
			Integer num = ps.executeUpdate();
			if (num == 0){
				rtn = 1;
			} else {
				rtn = 0;
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public List<Questionnaire> getQuestionnairesByUid(Integer uid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Questionnaire> qns = new ArrayList<Questionnaire>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,title,description,createtime,releasetime,path,type,status FROM oqp.questionnaire WHERE uid=? AND status!=2");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			while (rs.next()){
				Questionnaire qn = new Questionnaire();
				qn.setId(rs.getInt("id"));
				qn.setUid(uid);
				qn.setTitle(rs.getString("title"));
				qn.setDescription(rs.getString("description"));
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				qn.setCreateTime(fmt.parse(rs.getString("createtime")));
				qn.setType(rs.getInt("type"));
				qn.setStatus(rs.getInt("status"));
				if (qn.getStatus() == 1){
					qn.setReleaseTime(fmt.parse(rs.getString("releasetime")));
					qn.setPath(rs.getString("path"));
				}
				qns.add(qn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return qns;
	}
}
