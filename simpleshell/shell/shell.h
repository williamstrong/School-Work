//
//  main.c
//  shell
//
//  Created by William Strong on 10/7/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include <stdbool.h>



void execute_program(int, char *[], int, int);
void create_char_tokens(char *, int, char *[]);
int count_tokens(const char *);
bool valid_line( const char []);
void clear_stdin(char []);
void prompt_user(char *);
bool clear_stdin_if_line_invalid(char *);
bool quit(char *);
int set_file_in(char *);
int set_file_out(char *);
int check_for_in_redirection(int, char **);
int check_for_out_redirection(int, char **);
void fork_exec(int, char **);
void run_commands(char *, char **);
void process_input();