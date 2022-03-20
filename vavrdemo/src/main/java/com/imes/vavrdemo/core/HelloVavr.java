package com.imes.vavrdemo.core;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;

public class HelloVavr {
	public static void main(String[] args) {
		System.out.println("started");
		
		tuplesDemo();
		tryDemo();
		
		// options demo
		patternMatchDemo();
	}

	private static void patternMatchDemo() {
		Object car = new Car("BMW", 90);
		System.out.println("loaded: " + car);

		/*
		String carName = switch (car) {
			case Car c -> c.name;
			default -> null;
		};
		*/
		
		var engine = new Engine("BMW S55", 450, 600);
		System.out.println("engine: " + engine);
		System.out.println("engine power: " + engine.power());
	}

	private static void tryDemo() {
		String input = "1";
		
		Try<Integer>inputParsing = Try.of(() -> Integer.parseInt(input));
		System.out.println("input parsing result: " + inputParsing);
		
		var number = Match(inputParsing).of(
				Case($Success($()), value -> value));
		System.out.println("number: " + number);
	}

	private static void tuplesDemo() {
		Tuple2<String, Double> tuple = Tuple.of("recall", 0.22);
		
		System.out.println("java tuple: " + tuple);
	}
}
