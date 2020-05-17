package it.polito.tdp.crimes.db;

import java.sql.*;
import java.util.*;

import it.polito.tdp.crimes.model.CrimesCouple;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.MonthYear;


public class EventsDao {
	
	public void setAllEvents(Map<Long, Event> eIdMap){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if(!eIdMap.containsKey(res.getLong("incident_id"))) {
						Event e = new Event(res.getLong("incident_id"),
								res.getInt("offense_code"),
								res.getInt("offense_code_extension"), 
								res.getString("offense_type_id"), 
								res.getString("offense_category_id"),
								res.getTimestamp("reported_date").toLocalDateTime(),
								res.getString("incident_address"),
								res.getDouble("geo_lon"),
								res.getDouble("geo_lat"),
								res.getInt("district_id"),
								res.getInt("precinct_id"), 
								res.getString("neighborhood_id"),
								res.getInt("is_crime"),
								res.getInt("is_traffic"));
						eIdMap.put(e.getIncident_id(), e);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	public List<MonthYear> getMonths() {
		String sql = "SELECT DISTINCT MONTH(reported_date) AS m, YEAR(reported_date) AS y FROM EVENTS ORDER BY y, m" ;
		
		List<MonthYear> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				MonthYear my = new MonthYear(res.getInt("m"), res.getInt("y"));
				result.add(my);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<String> getCategories() {
		String sql = "SELECT DISTINCT offense_category_id AS c FROM events" ;
		
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String c = res.getString("c");
				result.add(c);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<Event> getAllCrimes(Map<Long, Event> eIdMap, String category, int year, int month){
		String sql = "SELECT incident_id FROM events " + 
				"WHERE offense_category_id=? AND YEAR(reported_date)=? AND MONTH(reported_date)=?" ;
		
		List<Event> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, category);
			st.setInt(2, year);
			st.setInt(3, month);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Event e = eIdMap.get(res.getLong("incident_id"));
					if(e!=null) {
						result.add(e);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<String> setVertices(Map<Long, Event> eIdMap, String category, int year, int month){
		String sql = "SELECT DISTINCT offense_type_id FROM events " + 
				"WHERE offense_category_id=? AND YEAR(reported_date)=? AND MONTH(reported_date)=?" ;
		
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, category);
			st.setInt(2, year);
			st.setInt(3, month);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					String s = res.getString("offense_type_id");
					result.add(s);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<CrimesCouple> setEdges(String category, int year, int month){
		String sql = "SELECT e1.offense_type_id AS c1, e2.offense_type_id AS c2, COUNT(DISTINCT(e1.neighborhood_id)) as w " + 
				"FROM events AS e1, events AS e2 " + 
				"WHERE e1.offense_category_id=? AND e2.offense_category_id=? " + 
				"AND MONTH(e1.reported_date)=? AND YEAR(e1.reported_date)=? " + 
				"AND MONTH(e2.reported_date)=? AND YEAR(e2.reported_date)=? " + 
				"AND e1.offense_type_id<e2.offense_type_id AND e1.neighborhood_id=e2.neighborhood_id " + 
				"GROUP BY e1.offense_type_id, e2.offense_type_id" ;
		
		List<CrimesCouple> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, category);
			st.setString(2, category);
			st.setInt(3, month);
			st.setInt(4, year);
			st.setInt(5, month);
			st.setInt(6, year);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
					CrimesCouple c = new CrimesCouple(res.getString("c1"), res.getString("c2"), res.getInt("w"));
					result.add(c);
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
