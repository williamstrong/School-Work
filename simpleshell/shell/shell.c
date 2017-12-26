//
//  main.c
//  shell
//
//  Created by William Strong on 10/7/17.
//  Copyright © 2017 William Strong. All rights reserved.
//
#include "shell.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/stat.h>

void clear_line() {
    // Extra insurance that stdin has nothing in the buffer.
    fseek(stdin, 0, SEEK_END);
}
void prompt_user(char * line) {
    // Prompt user and store input.
    printf("$ ");
    fflush(stdout);     // Insures that stdout is printed and not stored in the buffer.
    clear_line();
    fgets(line, sizeof(line)*80, stdin);
}

bool valid_line(const char line[]) {
    // Check for \n to see if line if over 80 chars. If it is stop execution and start again.
    bool valid_line = true;
    for (int char_in_line = 0; line[char_in_line] != '\0'; ++char_in_line) {
        if (line[char_in_line] == '\n') {
            break;
        }
        if (line[char_in_line] == '\0') {
            valid_line = false;
            break;
        }
    }
    return valid_line;
}
void clear_stdin( char line[]) {
    if (valid_line(line) == false) {
        fgets(line, sizeof(line), stdin);
    }
}
bool clear_stdin_if_line_invalid(char * line) {
    // Check for line length read stdin until empty if it is over 80 characters.
    // Returns true is line is invalid and buffer is clear, and false when line is valid.
    if(valid_line(line) == false) {
        printf("Invalid line\n");
        clear_stdin(line);
        return true;
    }
    return false;
}

bool quit(char * line) {
    // Check is the user has input the quit command.
    if (strcmp(line, "quit\n")==0) {
        printf("quiting\n");
        return true;
    }
    else return false;
}

char * remove_endline(char * token) {
    // For getting token without \n.
    return strtok(token, "\n");
}
int count_tokens(const char * tokens) {
    int count = 1;
    for (int i = 0; tokens[i] != '\0'; ++i) {
        if (tokens[i] == ' ') count++;
    }
    return count;
}
void create_char_tokens(char * tokens, int number, char * storage[]) {
    int i = 0;
    char * token;



    token = strtok(tokens, " ");
    if (number == 1) {
        remove_endline(token);
    }
    storage[i] = strdup(token);

    for (i=1; i < number; ++i) {
        token = strtok(NULL, " ");
        if (i == number-1) {
            token = remove_endline(token);
        }
        storage[i] = strdup(token);
    }
    storage[number] = NULL;
}

int set_file_in(char * file) {
    // Open file for reading.
    int in = open(file, O_RDONLY);
    if (in < 0) {
        printf("failed");
        return -1;
    }
    return in;
}
int set_file_out(char * file) {
    // Create write only file with R/W permissions for user.
    int out = open(file, O_WRONLY | O_CREAT, S_IWUSR | S_IRUSR);
    if (out < 0) {
        printf("failed");
        return -1;
    }
    return out;
}
int check_for_in_redirection(int number_of_tokens, char ** array) {
    // Look for < operator. Returns open file if < is present or NULL if < is not present.
    for (int i = 0; i < number_of_tokens; ++i) {
        if (strcmp(array[i], "<") == 0) {
            int open_infile = NULL;
            char *file = array[i + 1];
            open_infile = set_file_in(file);
            return open_infile;
        }
    }
    return NULL;
}
int check_for_out_redirection(int number_of_tokens, char ** array) {
    // Look for > operator. Returns open file if > is present or NULL if > is not present.
    for (int i = 0; i < number_of_tokens; ++i) {
        if (strcmp(array[i], ">") == 0) {
            int open_outfile = NULL;
            char *file = array[i + 1];
            open_outfile = set_file_out(file);
            return open_outfile;
        }
    }
    return NULL;
}
void execute_program(int len, char * param_list[], int file_in, int file_out) {
    // Runs within a child process. Final preparations to the paramaters, file redirection, and exec().
    char *parameters[len];
    memcpy(parameters,  param_list, sizeof(parameters));
    for (int i = 0; i < len; ++i) {
        if (strcmp(parameters[i], "<")==0 || strcmp(parameters[i], ">")==0) {
            parameters[i] = NULL;
        }
    }
    parameters[len] = NULL;
    if (file_in != NULL) {
        dup2(file_in, STDIN_FILENO);
        close(file_in);
    }
    if (file_out != NULL) {
        dup2(file_out, STDOUT_FILENO);
        close(file_out);
    }
    execvp(*parameters, parameters);
}
void fork_exec(int number_of_tokens, char ** array) {
    // Fork and run the command.
    int pid = fork();
    if (pid == 0) {
        int in = check_for_in_redirection(number_of_tokens, array);
        int out = check_for_out_redirection(number_of_tokens, array);
        execute_program(number_of_tokens, array, in, out);
    }
    if (pid > 0) {
        int status;
        wait(&status);
    }
    if (pid == -1) {
        printf("Child creation failed");
    }
}

void run_commands(char * line, char ** array) {
    // Prepares commands and runs them using fork_exec().
    int number_of_tokens = count_tokens(line);
    create_char_tokens(line, number_of_tokens, array);
    fork_exec(number_of_tokens, array);
}

void process_input() {
    // Main loop.
    char line[80];
    bool on = true;
    while(on) {
        // Stores result in line.
        prompt_user(line);

        // Check to see if the line is valid. If invalid clear stdin and continue.
        if (clear_stdin_if_line_invalid(line) == true) continue;

        if (quit(line) == true) on = false;
        else {
            char * token_array[count_tokens(line) + 1];
            run_commands(line, token_array);

        }
    }
}

int main() {
    process_input();
    return 0;
}