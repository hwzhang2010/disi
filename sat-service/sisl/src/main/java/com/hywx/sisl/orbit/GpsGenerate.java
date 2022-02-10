package com.hywx.sisl.orbit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;


import com.hywx.sisl.global.GlobalConstant;
import com.hywx.sisl.orbit.tle.prediction.Sgp4;
import com.hywx.sisl.orbit.tle.prediction.Tle;
import com.hywx.sisl.po.GpsFrame;
import com.hywx.sisl.util.TimeUtil;

public class GpsGenerate {
	
	public static void main(String[] args) {
		
		//Date date = new Date();
		
		//long timeStamp = 1615478400000L;  //时间戳, ms, 2021/03/12 00:00:00.000
		long timeStamp = 1615996800000L;  //时间戳, ms, 2021/03/18 00:00:00.000
		ZoneId zone = ZoneId.systemDefault();
		
		// GPS的卫星位置和速度
		Tle tle = new Tle("1 40930S 15052A   20288.34035880  .00000876  00000-0  34425-4 0  9993", "2 40930  86.5000  24.0000 0012549 124.0000   1.0000 13.4249506  67964");
		
		for (int i = 0; i < 86400; i++) {
			Date date = new Date(timeStamp + i * 1000);
			LocalDateTime localDateTime = Instant.ofEpochMilli(timeStamp + i * 1000).atZone(zone).toLocalDateTime();
			
			double[][] rv = tle.getRV(date);
			double[][] rvECEF = fromECItoECEF(date.getTime(), rv);
			
			//System.out.println("********************rvECEF:" + rvECEF[0][0] * 1000 + ", " + rvECEF[0][1] * 1000 + ", " + rvECEF[0][2] * 1000 + ", " + rvECEF[1][0] * 1000 + ", " + rvECEF[1][1] * 1000 + ", " + rvECEF[1][2] * 1000);
			
		    
			GpsFrame frame = new GpsFrame(localDateTime);
			frame.setSx(rvECEF[0][0] * 1000);
			frame.setSy(rvECEF[0][1] * 1000);
			frame.setSz(rvECEF[0][2] * 1000);
			frame.setVx(rvECEF[1][0] * 1000);
			frame.setVy(rvECEF[1][1] * 1000);
			frame.setVz(rvECEF[1][2] * 1000);
			
			WriteGps(LocalDateTime.now(), frame);
			
		}
		
		
		System.out.println("***********write gps file**********");
	}
	
	
	public static void WriteGps(LocalDateTime local, GpsFrame frame){
		
		try {
			File file = new File("D:\\home\\config\\disi\\tm\\gps.txt");
			if(!file.isFile()){
				file.createNewFile();
			} 
			// 追加写入
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"utf-8"));
			
			bw.write("\t1  ");
			bw.write(frame.getTime());
			bw.write("  ");
			bw.write(String.format("%.3f", frame.getSx()));
			bw.write("\t");
			bw.write(String.format("%.3f", frame.getSy()));
			bw.write("\t");
			bw.write(String.format("%.3f", frame.getSz()));
			bw.write("\t");
			bw.write(String.format("%.4f", frame.getVx()));
			bw.write("\t");
			bw.write(String.format("%.4f", frame.getVy()));
			bw.write("\t");
			bw.write(String.format("%.4f", frame.getVz()));
			bw.write("\t");
			bw.write(String.valueOf(TimeUtil.calJD(local)));
			bw.write("  ");
			bw.write(String.valueOf(TimeUtil.calJS(local)));
			bw.write("\r\n");
			
			
			bw.close();
		}catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	
	private static double[][] fromECItoECEF(long timeStamp, double[][] rv) {
		// 本地时间转UTC
		//long localTimeInMillis = current.getTime();
        /** long时间转换成Calendar */
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        //Date utcDate=new Date(calendar.getTimeInMillis());
		
        // 得到儒略日, 月份从0开始, 24小时制
		double[] jdut1 = Sgp4.jday(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		// 得到恒星时
		double gstime = Sgp4.gstime(jdut1[0] + jdut1[1]);
		
		// ECEF的坐标初值
		double r[] = new double[3];
        double v[] = new double[3];
		
		// 坐标系转换(位置)
		r[0] =  Math.cos(gstime) * rv[0][0] + Math.sin(gstime) * rv[0][1];
		r[1] = -Math.sin(gstime) * rv[0][0] + Math.cos(gstime) * rv[0][1];
		r[2] =  rv[0][2];
	    
	    // 坐标系转换(速度)
		v[0] =  Math.cos(gstime) * rv[1][0] + Math.sin(gstime) * rv[1][1] + GlobalConstant.WZ * r[1];
		v[1] = -Math.sin(gstime) * rv[1][0] + Math.cos(gstime) * rv[1][1] - GlobalConstant.WZ * r[0];
		v[2] =  rv[1][2];
		
		double[][] rvECEF = { r, v };

		return rvECEF;
	}
	
	
	

}
