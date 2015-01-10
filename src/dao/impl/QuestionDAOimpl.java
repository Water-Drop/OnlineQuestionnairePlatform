package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import dao.QuestionDAO;
import util.JDBCHelper;
import model.Question;

public class QuestionDAOimpl implements QuestionDAO{
	JDBCHelper jh = new JDBCHelper();
	public Integer addQuestion(Question q){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer qid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.question (qnid,title,description,createtime,order,type,status) VALUES (?,?,?,?,?,?,0)");
			ips.setInt(1,q.getQnid());
			ips.setString(2,q.getTitle());
			ips.setString(3,q.getDescription());
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			ips.setString(4, fmt.format(q.getCreateTime()));
			ips.setInt(5,q.getOrder());
			ips.setInt(6, q.getType());
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				qid = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return qid;
	}
	public Integer modifyQuestion(Question q){
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.question SET title=? AND description=? WHERE id=?");
			ps.setString(1,q.getTitle());
			ps.setString(2,q.getDescription());
			ps.setInt(3, q.getId());
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
	public Integer deleteQuestion(Integer qid){//set status=2
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.question SET states=2 WHERE id=?");
			ps.setInt(1, qid);
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
	public Integer getNewQuestionOrderByQnid(Integer qnid){
		return 0;
	}
}
