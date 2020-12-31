package com.hywx.sirs.service.impl;

import java.util.Vector;

import org.springframework.stereotype.Service;

import com.hywx.sirs.global.GlobalVector;
import com.hywx.sirs.service.ExchangeService;
import com.hywx.sirs.vo.ExchangeVO;

@Service("exchangeService")
public class ExchangeServiceImpl implements ExchangeService {

	@Override
	public Vector<ExchangeVO> getCurrentExchange() {
		
		return GlobalVector.getExchangeVector();
	}

	@Override
	public void putExchangeZero() {
		GlobalVector.updateZero();
	}

}
