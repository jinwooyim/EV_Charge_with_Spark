package com.boot.reservation.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boot.dto.MemberDTO;
import com.boot.reservation.dto.ReservationDTO;
import com.boot.reservation.service.ReservationService;

@Controller
public class ReservationController {

	@Autowired
	private ReservationService service;

	@RequestMapping("/reservation")
	public String reservation(@RequestParam("reservation_date") Date reservation_date, Model model) {
		List<ReservationDTO> reservation_list = service.find_reservation_by_reserve_date(reservation_date);

		model.addAttribute("reservation_list", reservation_list);

		return "reservation";
	}

	@RequestMapping("/reservation_ok")
	public String reservation_ok(@RequestParam("stat_id") String stat_id, @RequestParam("chger_id") String chger_id,
			@RequestParam("reservation_date") Date reservation_date,
			@RequestParam("reservation_date[]") Time[] reservation_time_list, HttpServletRequest request) {

		HttpSession session = request.getSession();
		MemberDTO user = (MemberDTO) session.getAttribute("user");
		int user_no = user.getUser_no();
		int duration_minutes = reservation_time_list.length * 30;
		Time reservation_time = reservation_time_list[1];

		service.insertReservation(stat_id, user_no, chger_id, reservation_date, reservation_time, duration_minutes);

		return "reservation_ok";
	}

	@GetMapping("/reservation/data")
	@ResponseBody
	public Map<String, Integer> getReservationData() {
		Map<String, Integer> data = new LinkedHashMap<>();
		data.put("09:00", 5);
		data.put("09:30", 7);
		data.put("10:00", 3);
		data.put("10:30", 10);
		data.put("11:00", 12);
		data.put("11:30", 6);
		return data;
	}
}
