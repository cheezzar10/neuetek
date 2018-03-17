#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// clang -std=c99 -Wall -o dummy dummy.c

int main(int argc, char* argv[]) {
	printf("started\n");
	
	int delay = 10;
	if (argc > 1) {
		delay = strtol(argv[1], NULL, 10);
	}
	
	printf("sleeping %d second(s)\n", delay);
	sleep(delay);
	
	printf("exiting...\n");
	return EXIT_SUCCESS;
}
