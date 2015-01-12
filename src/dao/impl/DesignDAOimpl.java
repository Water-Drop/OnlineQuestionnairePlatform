package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Design;
import util.JDBCHelper;
import dao.DesignDAO;

public class DesignDAOimpl implements DesignDAO{
	JDBCHelper jh = new JDBCHelper();
	public String getDatapathByUid(Integer uid){
		String rtn = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,path,status FROM oqp.design WHERE uid=? AND status!=2");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			if (rs.next() == true){
				rtn = rs.getString("path");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public Integer addDatapath(Design d){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer did = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.design (uid,path,status) VALUES (?,?,0)");
			ips.setInt(1,d.getUid());
			ips.setString(2,d.getDatapath());
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				did = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return did;
	}
}
