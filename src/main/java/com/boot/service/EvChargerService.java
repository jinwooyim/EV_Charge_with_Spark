package com.boot.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.boot.dto.EvChargerDTO;

public interface EvChargerService {
	public void ev_charger_update(List<EvChargerDTO> ev_charger_data);

	// 경도위도 근처 충전소 정보
	public List<EvChargerDTO> ev_list(@Param("lat") Double lat, @Param("lng") Double lng);

	// 충전소 id로 정보 가져오기
	public EvChargerDTO ev_list_by_stat(String stat_id);
}