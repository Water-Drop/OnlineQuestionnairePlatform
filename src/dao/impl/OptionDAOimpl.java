package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Option;
import util.JDBCHelper;
import dao.OptionDAO;

public class OptionDAOimpl implements OptionDAO{
	JDBCHelper jh = new JDBCHelper();
	public Integer addOption(Option o){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer oid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.option (qid,content,`order`,status) VALUES (?,?,?,0)");
			ips.setInt(1,o.getQid());
			ips.setString(2,o.getContent());
			ips.setInt(3,o.getOrder());
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				oid = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return oid;
	}
	public Integer modifyOption(Option o){
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.option SET content=?, `order`=? WHERE id=?");
			ps.setString(1,o.getContent());
			ps.setInt(2,o.getOrder());
			ps.setInt(3,o.getId());
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
	public Integer deleteOption(Integer oid){//set status=2
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.option SET status=2 WHERE id=?");
			ps.setInt(1, oid);
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
	public List<Option> getOptionsByQid(Integer qid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Option> os = new ArrayList<Option>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,content,`order`,status FROM oqp.option WHERE qid=? AND status!=2 ORDER BY `order`");
			ps.setInt(1, qid);
			rs = ps.executeQuery();
			while (rs.next()){
				Option o = new Option();
				o.setId(rs.getInt("id"));
				o.setQid(qid);
				o.setOrder(rs.getInt("order"));
				o.setStatus(rs.getInt("status"));
				os.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return os;
	}
	public Integer deleteAllOptions(Integer qid){
		Connection conn = null;
		PreparedStatement ps = null;
		Integer rtn = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("UPDATE oqp.option SET status=2 WHERE qid=?");
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
}
