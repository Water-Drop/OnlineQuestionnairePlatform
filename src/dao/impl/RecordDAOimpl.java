package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import util.JDBCHelper;
import model.AnswerRecord;
import model.FillRecord;
import dao.RecordDAO;

public class RecordDAOimpl implements RecordDAO{
	JDBCHelper jh = new JDBCHelper();
	public Integer addFillRecord(FillRecord fr){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer frid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.fillrecord (uid,qnid,filltime,status) VALUES (?,?,?,0)");
			ips.setInt(1,fr.getUid());
			ips.setInt(2,fr.getQnid());
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
			ips.setString(3,fmt.format(fr.getFillTime()));
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				frid = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return frid;
	}
	public List<FillRecord> getFillRecordsByUid(Integer uid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FillRecord> frs = new ArrayList<FillRecord>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,qnid,filltime,status FROM oqp.fillrecord WHERE uid=? AND status!=2");
			ps.setInt(1, uid);
			rs = ps.executeQuery();
			while (rs.next()){
				FillRecord fr = new FillRecord();
				fr.setId(rs.getInt("id"));
				fr.setUid(uid);
				fr.setQnid(rs.getInt("qnid"));
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				fr.setFillTime(fmt.parse(rs.getString("filltime")));
				fr.setStatus(rs.getInt("status"));
				frs.add(fr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return frs;
	}
	public List<FillRecord> getFillRecordsByQnid(Integer qnid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FillRecord> frs = new ArrayList<FillRecord>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,uid,filltime,status FROM oqp.fillrecord WHERE qnid=? AND status!=2");
			ps.setInt(1, qnid);
			rs = ps.executeQuery();
			while (rs.next()){
				FillRecord fr = new FillRecord();
				fr.setId(rs.getInt("id"));
				fr.setUid(rs.getInt("uid"));
				fr.setQnid(qnid);
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				fr.setFillTime(fmt.parse(rs.getString("filltime")));
				fr.setStatus(rs.getInt("status"));
				frs.add(fr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return frs;
	}
	public Integer isHaveFillRecords(Integer uid, Integer qnid){
		Integer rtn = 0;//not have
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,filltime,status FROM oqp.fillrecord WHERE uid=? AND qnid=? AND status!=2");
			ps.setInt(1, uid);
			ps.setInt(2, qnid);
			rs = ps.executeQuery();
			if (rs.first() == true){
				rtn = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return rtn;
	}
	public Integer addAnswerRecord(AnswerRecord ar){
		Connection conn = null;
		PreparedStatement ips = null;
		PreparedStatement sps = null;
		ResultSet rs = null;
		Integer arid = -1;
		try {
			conn = jh.getConnection();
			conn.setAutoCommit(false);
			ips = conn.prepareStatement("INSERT INTO oqp.answerrecord (fid,qid,qtype,oid,content,status) VALUES (?,?,?,?,?,0)");
			ips.setInt(1,ar.getFid());
			ips.setInt(2,ar.getQid());
			ips.setInt(3, ar.getQtype());
			if (ar.getQtype() == 1 || ar.getQtype() == 2){
				ips.setInt(4, ar.getOid());
				ips.setString(5, "");
			} else {
				ips.setInt(4, 0);
				ips.setString(5, ar.getContent());
			}
			sps = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			ips.execute();
			rs = sps.executeQuery();
			conn.commit();
			conn.setAutoCommit(true);
			rs.last();
			int count = rs.getRow();
			if (count != 0){
				rs.first();
				arid = Integer.parseInt(rs.getBigDecimal(1).toString());
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
		return arid;
	}
	public List<AnswerRecord> getAnswerRecordsByFid(Integer fid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AnswerRecord> ars = new ArrayList<AnswerRecord>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,qid,qtype,oid,content,status FROM oqp.answerrecord WHERE fid=? AND status!=2");
			ps.setInt(1, fid);
			rs = ps.executeQuery();
			while (rs.next()){
				AnswerRecord ar = new AnswerRecord();
				ar.setId(rs.getInt("id"));
				ar.setQid(rs.getInt("qid"));
				ar.setFid(fid);
				ar.setQtype(rs.getInt("qtype"));
				if (ar.getQtype() == 1 || ar.getQtype() == 2){
					ar.setOid(rs.getInt("oid"));
				} else {
					ar.setContent(rs.getString("content"));
				}
				ar.setStatus(rs.getInt("status"));
				ars.add(ar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return ars;
	}
	public List<AnswerRecord> getAnswerRecordsByQid(Integer qid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<AnswerRecord> ars = new ArrayList<AnswerRecord>();
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT id,fid,qtype,oid,content,status FROM oqp.answerrecord WHERE qid=? AND status!=2");
			ps.setInt(1, qid);
			rs = ps.executeQuery();
			while (rs.next()){
				AnswerRecord ar = new AnswerRecord();
				ar.setId(rs.getInt("id"));
				ar.setQid(qid);
				ar.setFid(rs.getInt("fid"));
				ar.setQtype(rs.getInt("qtype"));
				if (ar.getQtype() == 1 || ar.getQtype() == 2){
					ar.setOid(rs.getInt("oid"));
				} else {
					ar.setContent(rs.getString("content"));
				}
				ar.setStatus(rs.getInt("status"));
				ars.add(ar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return ars;
	}
	public Integer getFillCountByQnid(Integer qnid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer count = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT COUNT(*) FROM oqp.fillrecord WHERE qnid=? AND status!=2");
			ps.setInt(1, qnid);
			rs = ps.executeQuery();
			while (rs.first() != false){
				count = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return count;
	}
	public Integer getFillCountByOid(Integer oid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer count = -1;
		try {
			conn = jh.getConnection();
			ps = conn.prepareStatement("SELECT COUNT(*) FROM oqp.answerrecord WHERE oid=? AND status!=2");
			ps.setInt(1, oid);
			rs = ps.executeQuery();
			while (rs.first() != false){
				count = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jh.close(conn);
		}
		return count;
	}
}
