package com.hywx.sisl.vo;

import com.github.amsacode.predict4java.TLE;

public class OrbitElemVO {
	private String a;
	private String e;
	private String i;
	private String o;
	private String w;
	private String m;
	
	public OrbitElemVO() {
	}

	public OrbitElemVO(String a, String e, String i, String o, String w, String m) {
		this.a = a;
		this.e = e;
		this.i = i;
		this.o = o;
		this.w = w;
		this.m = m;
	}
	
	public OrbitElemVO(double a, double e, double i, double o, double w, double m) {
		this.a = String.valueOf(a);
		this.e = String.valueOf(e);
		this.i = String.valueOf(i);
		this.o = String.valueOf(o);
		this.w = String.valueOf(w);
		this.m = String.valueOf(m);
	}
	
	public void parse(TLE tle) {
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	public String getI() {
		return i;
	}

	public void setI(String i) {
		this.i = i;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	@Override
	public String toString() {
		return "OrbitElemVO: {a=" + a + ", e=" + e + ", i=" + i + ", o=" + o + ", w=" + w + ", m=" + m + "}";
	}
	
	
	
	

}
