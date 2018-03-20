package com.imc.rnd.procrun;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class ProcessRunnerCli {
	public static void main(String[] args) throws Exception {
		BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		
		while (true) {
			System.out.print("> ");
			String cmd = cmdReader.readLine();
			
			if ("exit".equals(cmd)) {
				return;
			} else if ("run".equals(cmd)) {
				runProcess();
			}
		}
	}

	private static void runProcess() throws Exception {
		String cwd = System.getProperty("user.dir");
		System.out.printf("working directory: %s%n", cwd);
		
		Path cwdPath = Paths.get(cwd);

		FileSystem zipFs = FileSystems.newFileSystem(cwdPath.resolve("input.data.zip"), 
				Thread.currentThread().getContextClassLoader());
		Path inputDataPath = zipFs.getPath("input.data");
		System.out.printf("stdio set to: %s%n", inputDataPath);
		File inputFile = new File(zipFs.toString());
		System.out.printf("f: %s%n", inputFile);
		Process proc = new ProcessBuilder(cwdPath.resolve("dummy").toString(), "3")
				.inheritIO()
				.redirectInput(ProcessBuilder.Redirect.from(inputFile))
				.start();
		
		boolean exited = proc.waitFor(2, TimeUnit.SECONDS);
		if (exited) {
			int exitStatus = proc.exitValue();
			System.out.printf("process completed - exit status: %d%n", exitStatus);
		} else {
			System.out.println("trying to terminate process");
			proc.destroyForcibly();
			
			// waiting one second for termination
			boolean terminated = proc.waitFor(1, TimeUnit.SECONDS);
			if (terminated) {
				int exitStatus = proc.exitValue();
				System.out.printf("process terminated - exit status %d%n", exitStatus);
			} else {
				System.out.println("process is still running");
			}
		}
		
	}
}
