package com.imes.vavrdemo.core;

import java.nio.file.Paths;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpDemo {
	public static void main(String[] args) throws Exception {
		JSch jsch = new JSch();
		
		Session session = jsch.getSession("andrey", "192.168.1.180");
		session.setPassword("password");
		
		Properties sessionConfig = new Properties();
		sessionConfig.put("StrictHostKeyChecking", "no");
		
		session.setConfig(sessionConfig);
		session.connect(); // session.disconnect()
		
		ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
		channel.connect(); // channel.disconnect()
		
		// System.out.println("entries: " + channel.ls("."));
		String sourcePath = Paths.get(System.getProperty("user.home"), "Downloads", "iChm.zip")
				.toAbsolutePath()
				.toString();

		channel.put(sourcePath, "ichm.zip");
	}
}
