package net.xfunction.java.api.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * MD5加密
 * 
 * @author liguo
 * 
 */
public final class MD5Util
{
	private static final char HEXDIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encode(File file)
	{
		FileInputStream in = null;
		MessageDigest md5 = null;
		try
		{
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
					0, file.length());
			md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				in.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return toHex(md5.digest());
	}

	public static String encode(String arg)
	{
		if (arg == null)
		{
			arg = "";
		}
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
			md5.update(arg.getBytes("UTF-8"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return toHex(md5.digest());
	}

	private static String toHex(byte[] bytes)
	{
		StringBuffer str = new StringBuffer(32);
		for (byte b : bytes)
		{
			str.append(HEXDIGITS[(b & 0xf0) >> 4]);
			str.append(HEXDIGITS[(b & 0x0f)]);
		}
		return str.toString();
	}

	public static void main(String[] args)
	{
		int id = 965;
		String password = "123456";
		System.out.println(MD5Util.encode(password));
		System.out.println(MD5Util.encode(id + MD5Util.encode(password)));
	}
}
