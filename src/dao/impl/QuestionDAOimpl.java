package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dao.QuestionDAO;
import util.JDBCHelper;
import model.Question;

public class QuestionDAOimpl implements QuestionDAO{
	public Integer addQuestion(Question q){
		JDBCHelper jh = new JDBCHelper();
			Connection conn = null;
			try {
				conn = jh.getConnection();
				PreparedStatement ps = conn
						.prepareStatement("INSERT qnid,title,description,type INTO oqp.question VALUES (?,?,?,?)");
				ps.setInt(1,q.getQnid());
				ps.setString(2,q.getTitle());
				ps.setString(3,q.getDescription());
				ps.executeQuery();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return 0;
	}
	public Integer modifyQuestion(Question q){
		JDBCHelper jh = new JDBCHelper();
		Connection conn = null;
		try {
			conn = jh.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("UPDATE oqp.question SET qnid=?,title=?,description=?,type=? WHERE id=?");
			ps.setInt(1,q.getQnid());
			ps.setString(2,q.getTitle());
			ps.setString(3,q.getDescription());
			ps.setInt(4, q.getType());
			ps.setInt(5, q.getId());
			ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
	public Integer deleteQuestion(Integer qid){
		JDBCHelper jh = new JDBCHelper();
		Connection conn = null;
		try {
			conn = jh.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("UPDATE oqp.question SET states=-1 WHERE id=?");
			ps.setInt(1, qid);
			ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
}
