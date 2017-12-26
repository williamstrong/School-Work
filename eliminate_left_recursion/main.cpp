#include <iostream>
#include <vector>
#include <sstream>

typedef std::pair<std::string, std::vector<std::string>> rule;

std::vector<std::string> KNOWN_SYMBOLS;

void find_left_recursion(std::vector<rule>);

std::vector<rule> fix_left_recursion(rule r);

void print_test(rule vector);

bool symbol_is_known(std::string const &symbol) {
    if (!KNOWN_SYMBOLS.empty()) {
        for (auto &x: KNOWN_SYMBOLS) {
            if (x == symbol) return true;
        }
    }
    return false;
}

void record_symbols(std::string s) {
    s.erase(std::remove_if(s.begin(), s.end(), isspace), s.end());
    for (auto x: s) {
        std::string tmp(std::string(1, x));
        if (symbol_is_known(tmp)) continue;
        else KNOWN_SYMBOLS.push_back(tmp);
    }
}

bool symbol_is_terminal(std::string symbol) {
    return islower(symbol[0]);
}

void prompt_user() {
    std::cout << "Enter a rule: ";
}

std::string input() {
    prompt_user();
    std::string in;
    std::getline(std::cin, in);
    return in;
}

rule split_input(const std::string &in) {
    rule ret_rule;

    std::istringstream iss(in);
    std::string nterm;
    iss >> ret_rule.first;

    std::string tmp_string;
    while (iss >> tmp_string) {
        ret_rule.second.push_back(tmp_string);
    }
    return ret_rule;
}

bool more_input() {
    std::cout << "More rules (y/n): ";
    std::string in;
    std::getline(std::cin, in);
    if (in == "y") return true;
    else if (in == "n") return false;
    else return more_input();
}

rule get_rule() {
    // Get input.
    std::string in = input();
    // Record new symbols in KNOWN_SYMBOLS.
    record_symbols(in);
    // Split input into non-terminal and string for rule.
    return split_input(in);
}

int main() {

    std::vector<rule> language;

    bool ask_for_input = true;
    while (ask_for_input) {
        language.push_back(get_rule());
        ask_for_input = more_input();
    }

    find_left_recursion(language);

    return 0;
}

void find_left_recursion(std::vector<rule> language) {
    std::vector<rule> r;
    bool rflag = true;
    for (auto x: language){
        for (auto y: x.second) {
            if (x.first.at(0) == y.at(0)) {
                // LEFT RECURSION
                r = fix_left_recursion(x);
                for (auto x: r) {
                    print_test(x);
                }
                rflag = false;
                break;
            }
        }
        if (rflag) {
            print_test(x);
        }
        rflag = true;
    }

}

void print_test(rule r) {
    // print test
   std::cout << r.first << " ->";
   for (auto y: r.second) {
       std::cout << " " << y;
   }
   std::cout << std::endl;

}

std::vector<rule> fix_left_recursion(rule r) {
    std::string new_nterm;
    new_nterm = r.first;
    new_nterm.append("'");

    std::vector<rule> new_rules;
    rule new_nterm_rule;
    rule nterm_rule;

    nterm_rule.first = r.first;
    new_nterm_rule.first = new_nterm;

    std::string tmp_str;
    for (auto x: r.second) {
        if (r.first.at(0) == x.at(0)) {
            // S' rule
            tmp_str = x.substr(1, x.size());
            tmp_str.append(new_nterm);

            new_nterm_rule.second.push_back(tmp_str);

        } else {
            // S rule
            tmp_str = x;
            tmp_str.append(new_nterm);

            nterm_rule.second.push_back(tmp_str);
        }
    }
    new_nterm_rule.second.push_back("Null");

    new_rules.push_back(nterm_rule);
    new_rules.push_back(new_nterm_rule);

    return new_rules;
}