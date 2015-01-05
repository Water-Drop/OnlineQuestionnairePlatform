package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dao.QuestionnaireDAO;
import util.JDBCHelper;
import model.Questionnaire;

public class QuestionnaireDAOimpl implements QuestionnaireDAO{
	public Integer addQuestionnaire(Questionnaire qn){
		JDBCHelper jh = new JDBCHelper();
		Connection conn = null;
		try {
			conn = jh.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("INSERT uid,title,description,type INTO oqp.questionnaire VALUES (?,?,?,?)");
			ps.setInt(1,qn.getUid());
			ps.setString(2,qn.getTitle());
			ps.setString(3,qn.getDescription());
			ps.setInt(4,qn.getType());
			ps.executeQuery();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}
}
