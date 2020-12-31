package com.hywx.sirs.service;

import java.util.Vector;

import com.hywx.sirs.vo.ExchangeVO;

public interface ExchangeService {
	
	Vector<ExchangeVO> getCurrentExchange();
	
	void putExchangeZero();

}
