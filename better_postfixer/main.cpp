#include <iostream>
#include <stack>
#include <string>
#include <algorithm>
#include <vector>

// Header
void s1(char);
void s2(char);
void s3(char);

void invalid_input(char);
int classify_token(char);
void reject();
void accept();
void rep(int);
void choose_state(char);
void machine(std::string);
void reset();
void print_out();


/** Holds ints corresponding to the type of token on the end of the stack.
     *
     * E: 1
     * Ep: 2
     * L: 3
     * Lp: 4
     * Ls: 5
     * +: 6
     * *: 7
     * Empty: 8
**/
std::stack<int> stack;

/*
 * Contains the numbers stored on the stack.
 * When the stack has a value of 1 the value can be found here.
 * Contains a size equal to the number of '1's in stack.
 */
//std::stack<char> stack_num;
std::vector<char> out;

// Remains true until input is invalid. Set false by reject().
bool valid = true;
// Remains false until s1 accepts. Set true by accept().
bool accepted = false;

// Holds values of 1, 2, and 3. Each representing the corresponding state.
int state = 1;

void invalid_input(char input) {
    std::cout << "Character " << input << " is invalid." << std::endl;
}
/**
 *
 * Legend:
 *  Digit: 1
 *  +: 2
 *  *: 3
 *  (: 4
 *  ): 5
 *  NULL: 6
 *
 * @param tok : a char token from a infix input.
 * @return  : an int corresponding to the type of token. Zero if invalid.
 */
int classify_token(char tok) {
    if (isdigit(tok)) return 1;
    else if (tok == '\0') return 6;
    else {
        switch (tok) {
            case '+':
                return 2;
            case '*':
                return 3;
            case '(':
                return 4;
            case ')':
                return 5;
            default:
                invalid_input(tok);
                return 0;
        }
    }
}

/**
 * switch (stack.top()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
 *
 */

/**
 *
 * Invalid input!
 *
 */
void reject() {
    std::cout << "The expression is rejected." << std::endl;
    valid = false;
}

void accept() {
    accepted = true;
}

void rep(int i) {
    stack.pop();
    stack.push(i);
}

void s1(char tok) {
    int c = classify_token(tok);

    switch (c) {
        case 1:
            // Token is a digit.
            switch (stack.top()) {
                case 1:
                    reject();
                    return;
                case 2:
                    reject();
                    return;
                case 3:
                    stack.push(1);
                    out.push_back(tok);
                    return;
                case 4:
                    stack.push(1);
                    out.push_back(tok);
                    return;
                case 5:
                    stack.push(1);
                    out.push_back(tok);
                    return;
                case 6:
                    stack.push(2);
                    out.push_back(tok);
                    return;
                case 7:
                    stack.pop();
                    out.push_back(tok);
                    out.push_back('*');
                    return;
                case 8:
                    stack.push(1);
                    out.push_back(tok);
                    return;
            }
        case 2:
            switch (stack.top()) {
                case 1:
                    stack.push(6);
                    return;
                case 2:
                    stack.push(7);
                    return;
                case 3:
                    reject();
                    return;
                case 4:
                    reject();
                    return;
                case 5:
                    reject();
                    return;
                case 6:
                    reject();
                    return;
                case 7:
                    reject();
                    return;
                case 8:
                    reject();
                    return;
            }
        case 3:
            switch (stack.top()) {
                case 1:
                    stack.push(7);
                    return;
                case 2:
                    stack.push(6);
                case 3:
                    reject();
                    return;
                case 4:
                    reject();
                    return;
                case 5:
                    reject();
                    return;
                case 6:
                    reject();
                    return;
                case 7:
                    reject();
                    return;
                case 8:
                    reject();
                    return;
            }
        case 4:
            switch (stack.top()) {
                case 1:
                    reject();
                    return;
                case 2:
                    reject();
                    return;
                case 3:
                    stack.push(3);
                    return;
                case 4:
                    stack.push(3);
                    return;
                case 5:
                    stack.push(3);
                    return;
                case 6:
                    stack.push(4);
                    return;
                case 7:
                    stack.push(5);
                    return;
                case 8:
                    stack.push(3);
                    return;
            }
        case 5:
            switch (stack.top()) {
                case 1:
                    stack.pop();
                    s3(tok);
                    return;
                case 2:
                    stack.pop();
                    s2(tok);
                    return;
                case 3:
                    reject();
                    return;
                case 4:
                    reject();
                    return;
                case 5:
                    reject();
                    return;
                case 6:
                    reject();
                    return;
                case 7:
                    reject();
                    return;
                case 8:
                    reject();
                    return;
            }
        case 6:
            switch (stack.top()) {
                case 1:
                    stack.pop();
                    s1(tok);
                    return;
                case 2:
                    stack.pop();
                    s2(tok);
                    return;
                case 3:
                    reject();
                    return;
                case 4:
                    reject();
                    return;
                case 5:
                    reject();
                    return;
                case 6:
                    reject();
                    return;
                case 7:
                    reject();
                    return;
                case 8:
                    accept();
                    return;
            }
        case 0:
            return;
        default:
            return;
    }
}

void s2(char tok) {
    int c = classify_token(tok);
    switch (c) {
        case 5:
            switch (stack.top()) {
                case 6:
                    stack.pop();
                    out.push_back('+');
                    s3(tok);
                    return;
                case 7:
                    stack.pop();
                    out.push_back('*');
                    state = 1;
                    return;
            }
        case 6:
            switch (stack.top()) {
                case 6:
                    stack.pop();
                    out.push_back('+');
                    s1(tok);
                    return;
                case 7:
                    reject();
                    return;
            }
    }
}

void s3(char tok) {
    switch (stack.top()) {
        case 1:
            stack.pop();
            s3(tok);
            return;
        case 3:
            rep(1);
            state = 1;
            return;
        case 4:
            rep(1);
            state = 1;
            return;
        case 5:
            stack.pop();
            s2(tok);
            return;
        case 8:
            reject();
            return;
    }
}


void choose_state(char tok) {
    switch (state) {
        case 1:
            s1(tok);
            return;
        case 2:
            s2(tok);
            return;
        case 3:
            s3(tok);
            return;
        default:
            return;
    }
}

void print_out() {
    std::cout << "The expression is accepted. It evaluates to the postfix expression: ";
    std::cout.flush();
    for (auto x: out) {
        std::cout << x << " ";
        std::cout.flush();
    }
    std::cout << std::endl;

}

void machine(std::string in) {
    stack.push(8);
    for (auto x: in) {
        if (!valid) return;
        choose_state(x);
    }
    // Null will mark the end of the line.
    choose_state('\0');
    if (accepted) {
        print_out();
        return;
    }
}

void reset() {
    state = 1;
    out.clear();
    valid = true;
    accepted = false;

    // Pop the stack until empty or 8.
    while (stack.top() != 8 && !stack.empty()) {
        stack.pop();
    }
    // Precaution in case something goes terribly wrong.
    if (stack.empty()) stack.push(8);
}

int main() {
    std::string in;

    while (true) {
        std::cout << "Enter an infix expression: ";
        std::cout.flush();

        std::getline(std::cin, in);

        in.erase(std::remove_if(in.begin(), in.end(), isspace), in.end());


        machine(in);



        while (true) {
            std::cout << "More expressions? (y/n): ";
            std::cout.flush();
            std::getline(std::cin, in);
            if (in == "n") return 0;
            else if (in == "y") break;
            else continue;
        }


        reset();
    }
}
