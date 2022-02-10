package com.hywx.sisl.orbit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hywx.sisl.orbit.tle.TLE;
import com.hywx.sisl.orbit.tle.TLEBuilder;




/**  
 *  @Project       : testObject;
 *  @Program Name  : .COE2TLEConversion.java;
 *  @Class Name    : COE2TLEConversion;
 *  @Description   : ;
 *  @Author        : shilusha;
 *  @since Ver 1.0
 *  @Creation Date : 2017-5-3 下午4:45:15 ;
 *  @ModificationHistory  ;
 *  who        When          What ;
 *  --------   ----------    -----------------------------------;
 *  username   2017-5-3       TODO;
 */;
public class COE2TLEConversionAlg {
	
	/** ;
	 *  @Description    :卫星开普勒六根数转换到两行轨道根数;
	 *  @Method_Name    : simpleCoe2Tle;
	 *  @param startYear:开始年
	 *  @param startTime:开始时间，每年1月1日0点为0，后逐渐累积，整数部分为日，小数部分为时分秒
	 *  @param six		:轨道六根数,顺序为
	 *     	Mean				:平均运动（每日绕行圈数）
	 *  	Eccentricity		:离心率（小数）
	 *  	Inclination			:轨道的交角（deg）
	 *  	Argument of perigee :近地点角矩(deg)
	 *  	RAAN				:升交点赤经（deg）
	 *  	Mean				:在轨圈数
	 *  @param name		:卫星两位数编号，如01
	 *  @return 		:两行轨道根数
	 *  @return         : String[];
	 *  @Creation Date  : 2017-5-3 下午4:45:09 ;
	 *  @version        : v1.0;
	 *  @Author         : ; 
	 *  @Update Date    : 
	 *  @Update Author  : ;
	 */;
	public String[] simpleCoe2Tle(int startYear,double startTime,double[] six,String name){
		String[] tle = new String[2];
		StringBuffer sBuffer0=new StringBuffer("1 ");
		//行号+卫星编号
		StringBuffer sBuffer1=new StringBuffer("2 ");
		String sTemp;//临时变量，存储six数组中转换后的String
		int length;//临时变量，存储sTemp的长度
		sTemp=name;
		length=sTemp.length();
		if(length==5){
			sBuffer0.append(sTemp);
			sBuffer1.append(sTemp);	
		}
		else if(length<5){
			for(int i=0;i<5-length;i++){
				sBuffer0.append("0");
				sBuffer1.append("0");
			}
			sBuffer0.append(sTemp);
			sBuffer1.append(sTemp);
		}
		else{
			System.out.println("satellite name error");
		}
		sBuffer0.append("U          "+startYear+Double.toString(startTime).substring(0, 12)+"  .00000000  00000-0  00000-0 0 0000");
		sBuffer1.append(" ");
		//轨道的交角six[2]
		sTemp=Double.toString(six[2]);
		length=sTemp.length();
		if(six[2]<10){
			sBuffer1.append("00");
			
			if(length>=7){
				sBuffer1.append(sTemp.substring(0, 6));
			}else{
				sBuffer1.append(sTemp);
				for(int i=0;i<6-length;i++){
					sBuffer1.append("0");
				}
			}
			
		}
		else if((six[2]<100)&&(six[2]>10)){	
			sBuffer1.append("0");
			if(length>=8){
				sBuffer1.append(sTemp.substring(0, 7));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<7-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		else{
			if(length>=9){
				sBuffer1.append(sTemp.substring(0, 8));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<8-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		sBuffer1.append(" ");
		//升交点赤经six[4]
		sTemp=Double.toString(six[4]);
		length=sTemp.length();
		if(six[4]<10){
			sBuffer1.append("00");
			
			if(length>=7){
				sBuffer1.append(sTemp.substring(0, 6));
			}else{
				sBuffer1.append(sTemp);
				for(int i=0;i<6-length;i++){
					sBuffer1.append("0");
				}
			}
			
		}
		else if((six[4]<100)&&(six[4]>10)){
			sBuffer1.append("0");
			if(length>=8){
				sBuffer1.append(sTemp.substring(0, 7));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<7-length;i++){
					sBuffer1.append("0");
				}
			}
			
		}
		else{
			if(length>=9){
				sBuffer1.append(sTemp.substring(0, 8));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<8-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		sBuffer1.append(" ");
		//离心率six[1]
		sTemp=Double.toString(six[1]);
		sTemp=sTemp.substring(2);
		length=sTemp.length();
		if(length>=9){
			sBuffer1.append(sTemp.substring(0, 7));
		}
		else{
			sBuffer1.append(sTemp);
			for(int i=0;i<7-length;i++){
				sBuffer1.append("0");
			}
		}
		sBuffer1.append(" ");
		//近地点角矩
		sTemp=Double.toString(six[3]);
		length=sTemp.length();
		if(six[3]<10){
			sBuffer1.append("00");
			
			if(length>=7){
				sBuffer1.append(sTemp.substring(0, 6));
			}else{
				sBuffer1.append(sTemp);
				for(int i=0;i<6-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		else if((six[3]<100)&&(six[3]>10)){
			sBuffer1.append("0");
			if(length>=8){
				sBuffer1.append(sTemp.substring(0, 7));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<7-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		else{
			if(length>=9){
				sBuffer1.append(sTemp.substring(0, 8));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<8-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		sBuffer1.append(" ");
		//在轨圈数
		sTemp=Double.toString(six[5]);
		length=sTemp.length();
		if(six[5]<10){
			sBuffer1.append("00");
			
			if(length>=7){
				sBuffer1.append(sTemp.substring(0, 6));
			}else{
				sBuffer1.append(sTemp);
				for(int i=0;i<6-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		else if((six[5]<100)&&(six[5]>10)){
			sBuffer1.append("0");
			if(length>=8){
				sBuffer1.append(sTemp.substring(0, 7));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<7-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		else{
			if(length>=9){
				sBuffer1.append(sTemp.substring(0, 8));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<8-length;i++){
					sBuffer1.append("0");
				}
			}
		}
		sBuffer1.append(" ");
		//平均运动（每日绕行圈数）
		sTemp=Double.toString(six[0]);
		length=sTemp.length();
		if(six[0]<10){
			sBuffer1.append("0");
			if(length>=16){
				sBuffer1.append(sTemp.substring(0, 15));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<15-length;i++){
					sBuffer1.append("0");
				}
			}
			//tle[1]=tle[1]+"0"+Double.toString(six[0]).substring(0, 14)+" ";
		}
		else{
			if(length>=17){
				sBuffer1.append(sTemp.substring(0, 16));
			}
			else{
				sBuffer1.append(sTemp);
				for(int i=0;i<16-length;i++){
					sBuffer1.append("0");
				}
			}
			/*tle[1]=tle[1]+Double.toString(six[0]).substring(0, 15)+" ";*/
		}
		
		
		tle[0]=sBuffer0.toString();
		tle[1]=sBuffer1.toString();
		//第一行校验
		int sum=0;
		for(int i=0;i<tle[0].length();i++){
			String subTLE=tle[0].substring(i, i+1);
			if((subTLE.equals("U"))||(subTLE.equals(" "))||(subTLE.equals("."))||(subTLE.equals("+"))){
				sum+=0;
			}else if((subTLE.equals("-"))){
				sum+=1;
			}else{
				sum=sum+Integer.valueOf(subTLE);
			}
		}
		//System.out.println(sum);
		//第二行校验
		tle[0]=tle[0]+sum%10;
		sum=0;
		for(int i=0;i<tle[1].length();i++){
			String subTLE=tle[1].substring(i,i+1);
			if((subTLE.equals("U"))||(subTLE.equals(" "))||(subTLE.equals("."))||(subTLE.equals("+"))){
				
			}else if((subTLE.equals("-"))){
				sum+=1;
			}else{
				sum=sum+Integer.parseInt(subTLE);
			}
		}
		//System.out.println(sum);
		tle[1]=tle[1]+sum%10;
		return tle;
	}
	
	public static void main(String[] args) throws ParseException{
//		int startYear = 17;
//		double startTime=123.16666667;//长度固定
//		double[] six=new double[6];
//		six[0]=3;//必须小于100
//		six[1]=0.001;//小于1
//		six[2]=28.5;//不能是负数
//		six[3]=222;//不超过360
//		six[4]=111;//不超过360
//		six[5]=123;//不超过360
//		String name="01";//最多五位数
//		
//		String[] tle=new COE2TLEConversionAlg().simpleCoe2Tle(startYear,startTime,six,name);
//		System.out.println(tle[0]);
//		System.out.println(tle[1]);
		
//		int startYear = 20;
//		double startTime=287.67369213;//长度固定
//		double[] six=new double[6];
//		six[0]=15.5283968;//必须小于100
//		six[1]=0.0012549;//小于1
//		six[2]=51.6410;//不能是负数
//		six[3]=97.8883;//不超过360
//		six[4]=118.4439;//不超过360
//		six[5]=308.7451;//不超过360
//		String name="0004";//最多五位数
//		
//		String[] tle=new COE2TLEConversionAlg().simpleCoe2Tle(startYear,startTime,six,name);
//		System.out.println(tle[0]);
//		System.out.println(tle[1]);
		
		
//		String[] tleStr = {
//				"ISS (ZARYA)",
//				"1 25544U 98067A   20200.20503472  .00000290  00000-0  13213-4 0  9992",
//				"2 25544  51.6421 194.9479 0001278 129.0349  50.4204 15.49516142236824"
//			};
//		
//		TLE tle = new TLE(tleStr);
//		
//		Satellite satellite = SatelliteFactory.createSatellite(tle);
//		
//		GroundStationPosition groundStationPosition = new GroundStationPosition(48.85333, 2.34861, 30);
//		
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		String strDate="2020/10/14 14:03:39";
//		
//		try {
//			SatPos satPos = satellite.getPosition(groundStationPosition, sdf.parse(strDate));
//			System.err.println(satPos);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		
		
		int satelliteNumber = 40930;
		String internationalDesignator = "15052A";
		long epoch = 1634659200000L; // Milliseconds since Jan 1, 1970 00:00:00
		
//		TLE tle = TLEBuilder.newBuilder("GW0305")
//				.setSatelliteNumber(40930)
//                .setInternationalDesignator("15052A")
//                .setEpoch(1602663007000L)
//                .setElementSetNumber(999)
//                .setOrbitalElements(91.6410, 118.4439, 0.0012549, 97.8883, 200.7451)
//                .setRevolutions(6796)
//                .setMeanMotion(15.5283968)
//                .setFirstDerivativeMeanMotion(.00000876)
//                .setDragTerm(.000034425)
//                .setClassification('S')
//                .build();
//		System.out.println(tle);
		
//		double start = 128.7451;
//		for (int i = 0; i < 20; i++) {
//			String num = String.format("GW010%X", i + 1);
//			
//			double current = start + 18 * i;
//			if (current > 360)
//				current -= 360;
//			
//			TLE tle = TLEBuilder.newBuilder(num)
//			 				  .setSatelliteNumber(40930)
//			 			      .setInternationalDesignator("15052A")
//							  .setEpoch(1602663007000L)
//			 			      .setElementSetNumber(999)
//			 		          .setOrbitalElements(86.5, 24.0, 0.0001, 124.000, current)
//			 		          .setRevolutions(6796)
//			 	              .setMeanMotion(13.424950599670)
//			 			      .setFirstDerivativeMeanMotion(.00000876)
//			 			      .setDragTerm(.000034425)
//			 			      .setClassification('S')
//			 			      .build();
//			
//			System.out.println(tle);
//		}
		

		for (int k = 0; k < 15; k++) {
			double start = 19;
			for (int i = 0; i < 20; i++) {
				String num = String.format("GW%02X%02X", k+1, i + 1);
				
				double current = start + 18 * i;
				if (current > 360)
					current -= 360;
				
				double l = 24.0 + 24 * k;
				if (l >= 360)
					l -= 360;
				
				TLE tle = TLEBuilder.newBuilder(num)
				 				  .setSatelliteNumber(40930)
				 			      .setInternationalDesignator("15052A")
								  .setEpoch(1634659200000L)
				 			      .setElementSetNumber(999)
				 		          .setOrbitalElements(86.5, l, 0.0001, 124.000, current)
				 		          .setRevolutions(6796)
				 	              .setMeanMotion(13.424950599670)
				 			      .setFirstDerivativeMeanMotion(.00000876)
				 			      .setDragTerm(.000034425)
				 			      .setClassification('S')
				 			      .build();
				
				System.out.println(String.format("%02X%02X", k+1, i + 1) + "," + tle);
			}		
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
