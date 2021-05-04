package edu.onef.mem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import sun.misc.Signal;
import sun.misc.SignalHandler;
import sun.misc.Unsafe;

// -server -Xms128m -Xmx512m -XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics
public class Allocator {
	private static final int MBYTE = 1024 * 1024;
	
	private static volatile boolean terminated = false;
	
	private static final Signal SIGTERM = new Signal("TERM");

	public static void main(String[] args) {
		int pid = getPid();

		System.out.printf("memory allocator processes started - pid: %d%n", pid);

		System.out.println("allocating memory block");
		
		byte[] block = new byte[MBYTE];
		
		System.out.printf("allocated memory block of size %d bytes%n", block.length);
		
		System.out.println("processing started");
		
		registerHeapDumpShutdownHook(pid);

		while (!terminated) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException intrEx) {
				break;
			}
		}
		
		System.out.println("memory allocator process exited");
	}
	
	private static Unsafe getUnsafe() {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static void runHeapDumpGenerationProcess(int pid) {
		try {
			System.out.println("starting at exit heap dump generation process");
			
			Process heapDumpProc = 
					new ProcessBuilder("jmap", String.format("-dump:live,format=b,file=/tmp/allocator_%d.hprof", pid), "" + pid)
					.inheritIO()
					.start();
			
			int heapDumpProcExitCode = heapDumpProc.waitFor();
			
			System.out.printf("heap dump generation process completed with status: %d%n", heapDumpProcExitCode);
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		} catch (InterruptedException intrEx) {
			intrEx.printStackTrace();
		}
	}
	
	private static void registerHeapDumpShutdownHook(int pid) {
		Unsafe unsafe = getUnsafe();
		
		SignalHandler stockTermSignalHandler = Signal.handle(SIGTERM, new SignalHandler() {
			@Override
			public void handle(Signal signal) {
				System.out.println("memory allocator process terminated");

				terminated = true;
				
				System.exit(143);
			}
		});
		
		Signal.handle(SIGTERM, new SignalHandler() {
			@Override
			public void handle(Signal signal) {
				System.out.println("TERM signal trapped");
				
				runHeapDumpGenerationProcess(pid);
				
				stockTermSignalHandler.handle(signal);
			}
		});
		
		/*
		runtime.addShutdownHook(new Thread() {
			@Override
			public void run() {
				runHeapDumpGenerationProcess(pid);
			}
		});
		*/
	}

	private static final int getPid() {
		try (FileReader selfStatusFile = new FileReader("/proc/self/status");
				BufferedReader selfStatusReader = new BufferedReader(selfStatusFile)) {
			
			String line = null;
			while ((line = selfStatusReader.readLine()) != null) {
				if (line.contains("Pid")) {
					String[] pidLine = line.split(":");
					return Integer.parseInt(pidLine[1].trim());
				}
			}
			
			return -1;
		} catch (IOException ioEx) {
			ioEx.printStackTrace();

			return -1;
		}
	}
}
