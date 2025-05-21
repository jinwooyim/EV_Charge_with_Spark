package com.boot.reservation.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.boot.reservation.dto.ReservationDTO;

public interface ReservationService {
	public void insertReservation(String stat_id, int user_no, String chger_id, Date reservation_date,
			Time reservation_time, int duration_minutes);

	public int find_reservation_by_stat(String stat_id, String chger_id);

	public int find_reservation_by_user(int user_no);

	public List<ReservationDTO> find_reservation_by_reserve_date(Date reservation_date);
}
