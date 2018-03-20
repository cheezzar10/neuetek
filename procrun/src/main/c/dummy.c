#define _GNU_SOURCE

#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <unistd.h>

// clang -std=c99 -Wall -o dummy dummy.c
const size_t RD_BUF_SZ = 4096;

void signal_handler(int sig) {
	printf("trapped: %d\n", sig);
	_exit(33);
}

int main(int argc, char* argv[]) {
	struct sigaction sig_act;

	sig_act.sa_handler = signal_handler;
	sigemptyset(&sig_act.sa_mask);
	sig_act.sa_flags = 0;

	sigaction(SIGINT, &sig_act, NULL);
	sigaction(SIGQUIT, &sig_act, NULL);
	sigaction(SIGTERM, &sig_act, NULL);
	
	printf("started\n");

	char rd_buf[RD_BUF_SZ];

	ssize_t bts_read, bts_read_total = 0;
	while ((bts_read = read(STDIN_FILENO, rd_buf, RD_BUF_SZ)) > 0) {
		bts_read_total += bts_read;
	}
	printf("%d bytes read from stdio\n", bts_read_total);
	
	int delay = 10;
	if (argc > 1) {
		delay = strtol(argv[1], NULL, 10);
	}
	
	printf("sleeping %d second(s)\n", delay);
	sleep(delay);
	
	printf("exiting...\n");
	return EXIT_SUCCESS;
}
