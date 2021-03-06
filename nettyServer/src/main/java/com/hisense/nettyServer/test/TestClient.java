package com.hisense.nettyServer.test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.hisense.nettyServer.support.util.Enums;
import com.hisense.nettyServer.support.util.XML2String;

/*
 * 模拟前端设备发出的数据
 */
/**
 * @author qiu 
**/
public class TestClient {

	public static void main(String[] args)  {
		
		Socket socket = null;
		try {
			String path = "src/main/resources/allXML/Z_3014-243141_1_20181010110003350_川A78V6Y.XML";
			String s = XML2String.XMLToString(path);
			System.out.println(s);
			//String s = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><Message><MessageHeader><Version>1.0</Version><From>VRoad</From><System /></MessageHeader><MessageBody Type=\"PlateInfo\"><DeviceId>3013-722003</DeviceId><EPDevId>510100000000A3KBZ7</EPDevId><Location>二环高架路内侧7km+128m处</Location><VehicleType>1</VehicleType><PlateNo>------0</PlateNo><PlateColor>2</PlateColor><Confidence>93.570000</Confidence><Direction>2B</Direction><LaneNo>1</LaneNo><Speed>44</Speed><VehKnd comment=\"车辆种类(0:大客车;1:小轿车;2:面包车;3:小货车;4:客车;5:货车)\">5</VehKnd><VehBrand /><VehColor comment=\"车辆颜色(0:其他(灰);1:白;2:灰;3:黑;4:红;5:紫;6:蓝;7:绿;8:黄;9:棕;10:粉)\">3</VehColor><VehNewEnerg comment=\"新能源标志(0:非新能源;1:新能源小车;2:新能源大车)\">0</VehNewEnerg><BigPlateNo comment=\"放大号牌\" /><CapTime>2017-12-28 10:00:37.586</CapTime></MessageBody></Message>";
			int slen = s.getBytes().length;
			//长度为905
			// Alert(slen);
			byte[] b = new byte[5];

			b[0] = 0x01;
			long signed = ((slen & 255) / 128) & 0x0FFFFFFFF;
			b[1] = (byte) ((slen & 255) - signed * 256);

			signed = (((slen & 65280) >> 8) / 128) & 0x0FFFFFFFF;
			b[2] = (byte) ((slen & 65280) >> 8 - signed * 256);
			// Alert(b[2]);
			signed = (((slen & 16711680) >> 16) / 128) & 0x0FFFFFFFF;
			b[3] = (byte) ((slen & 16711680) >> 16 - signed * 256);
			// Alert(b[3]);
			// long mask = 4278190080l;
			signed = (((slen & 4278190080L) >> 24) / 128) & 0x0FFFFFFFF;
			b[4] = (byte) ((slen & 4278190080L) >> 24 - signed * 256);
			// Alert(b[4]);
			// b[FileSize + 5] = 0x0;

			// 对服务端发起连接请求
			socket = new Socket("192.168.56.1", 12001);
			OutputStream os = socket.getOutputStream();

			BufferedOutputStream bos = new BufferedOutputStream(os);
			// s
			byte[] buf = new byte[(slen + 6)];
			System.arraycopy(b, 0, buf, 0, 5);
			System.arraycopy(s.getBytes(), 0, buf, 5, slen);

			buf[slen + 5] = 0;

			bos.write(buf, 0, slen + 6);
			bos.flush();
			socket.close();
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
