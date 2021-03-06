package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ufo.model.Annata;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public Map<Integer,Sighting> getSightings(int anno,Map<Integer,Sighting> idMap) {
		String sql = "SELECT *  " + 
				"FROM sighting " + 
				"WHERE country='us' AND YEAR(DATETIME)=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			Map<Integer,Sighting> mappa = new HashMap<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if (!idMap.containsKey(res.getInt("id"))) {
					mappa.put(res.getInt("id"),new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;}
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return mappa ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Annata> getAnnate() {
		String sql ="SELECT YEAR(DATETIME) as anno, COUNT(*) AS c " + 
				"FROM sighting " + 
				"GROUP BY YEAR(DATETIME)" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
		
			
			ResultSet res = st.executeQuery() ;
			List<Annata> l= new LinkedList<Annata>();
			while(res.next()) {
				try {
					int anno = res.getInt("anno");
					int c = res.getInt("c");
					l.add(new Annata(anno,c));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return l ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	/*public List<Integer> getSuccessivi(int anno,LocalDateTime time,Map<Integer,Sighting> idMap ) {
		String sql = "SELECT id  " + 
				"FROM sighting " + 
				"WHERE country='us' AND YEAR(datetime)=? AND DATEDIFF(sighting.datetime , ?)>0 " ;
		try {
			Connection conn = DBConnect.getConnection() ;
            //int anno = date_posted.getYear();
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDate(2,Date.valueOf(time.toLocalDate()));
			st.setInt(1, anno);
			List <Integer> l = new LinkedList<>();
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
			//		System.out.println(res.getInt("id"));
					if (idMap.containsKey(res.getInt("id"))) {
					l.add(res.getInt("id"));
					}
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return l ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}*/

}
