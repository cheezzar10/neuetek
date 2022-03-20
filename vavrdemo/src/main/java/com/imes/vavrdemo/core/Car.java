package com.imes.vavrdemo.core;

public class Car {
	public String name;
	public int speed;

	public Car(String name, int speed) {
		this.name = name;
		this.speed = speed;
	}

	@Override
	public String toString() {
		return "Car{name=" + name + ", speed=" + speed + "}";
	}
}
