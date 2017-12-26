//
// Created by William Strong on 10/19/17.
//

#pragma once

#if _WIN32
#include <stdio.h>
#include <tchar.h>
#endif

#include <fstream>
#include <vector>
#include <sstream>
#include <algorithm>
#include <iterator>
#include <iostream>


// Parser.h
class Parser
{
public:
    // Add members for testing here.
    std::vector<std::string> split_by_token(std::string);	// static for testing

    explicit Parser(std::string);
    virtual ~Parser();
    std::vector<std::string> token_vector;
    std::vector<int> line_number_vector;

    // Iterators for use by children.
    std::vector<std::string>::iterator token_begin;
    std::vector<std::string>::iterator token_end;
    std::vector<int>::iterator line_begin;
    std::vector<int>::iterator line_end;
    std::string file_name;

protected:
    std::vector<std::string> split_tokens {
            ":=",
            "+",
            "-",
            "*",
            ",",
            ".",
            ";",
            ":",
            "(",
            ")" };

private:
    void count_lines(unsigned long, int);
    void load_line_into_vector();
//	void split_into_language_tokens(std::vector<std::string>&);
    std::vector<std::string> split_into_language_tokens(std::vector<std::string>&);
    //std::vector<std::string> split_by_token(std::string);	// static for testing
    void create_iterators();
};


// Scanner.h
class Scanner : public Parser
{
public:
    Scanner(std::string);
    ~Scanner() = default;
    std::vector<int> token_code_vector;

private:
    std::vector<std::string>::iterator it;

    // A sorted list of keywords that exist in the language. Can search through using Binary search.
    // May be a more efficient way to make this happen.

    bool is_keyword(std::string);
    bool is_identifier(std::string);
    bool is_operator(std::string);
    bool is_delimiter(std::string);
    bool is_numeric_constant(std::string);
    void iterate_through_tokens();
    void output_to_file();

    std::set<std::string> keywords {
            "begin",
            "div",
            "do",
            "end",
            "for",
            "integer",
            "program",
            "read",
            "to",
            "var",
            "write" };
    std::set<std::string> operators {
            ":=",
            "+",
            "-",
            "*" };
    std::set<std::string> delimiters {
            ",",
            ".",
            ";",
            ":",
            "(",
            ")" };

};


// Parser.cpp
Parser::Parser(std::string in_file_name)
{
    file_name = std::move(in_file_name);
    load_line_into_vector();
    create_iterators();
}


Parser::~Parser() = default;

void Parser::load_line_into_vector()
{
    std::ifstream file(file_name);
    if (!file) std::cout << "failed" << std::endl;

    std::string line;
    std::istringstream iss;
    int line_number = 1;
    std::vector<std::string> tmp_vector;
    std::vector<std::string> app_vector;

    while (!file.eof())
    {
        // Uses a stringstream to copy all of the line into a vector seperated by spaces. Calls
        std::getline(file, line);
        iss.str(line);

        std::copy(
                std::istream_iterator<std::string>(iss),
                std::istream_iterator<std::string>(),
                std::back_inserter(tmp_vector));
        app_vector = split_into_language_tokens(tmp_vector);
        token_vector.reserve(token_vector.size()+app_vector.size());
        token_vector.insert(token_vector.end(), app_vector.begin(), app_vector.end());

        count_lines(tmp_vector.size(), line_number);

        // Prepare for next loop.
        tmp_vector.clear();
        iss.clear();
        line_number++;
    }

}

void Parser::count_lines(unsigned long lines, int line_num)
{
    for (int i = 0; i < lines; ++i) line_number_vector.push_back(line_num);
}


std::vector<std::string> Parser::split_into_language_tokens(std::vector<std::string>& vector)
{
    std::string cpy_str;
    std::vector<std::string> new_vect;
    std::vector<std::string> tmp_vect;
    for (auto& x: vector) {
        cpy_str = x;
        tmp_vect = split_by_token(cpy_str);
        new_vect.insert(new_vect.end(), tmp_vect.begin(), tmp_vect.end());
    }
    return new_vect;
}


std::vector<std::string> Parser::split_by_token(std::string str)
{
    size_t position;
    std::string token;
    std::string next;
    std::vector<std::string> new_vector;
    std::vector<std::string> next_vector;

    if (std::find(split_tokens.begin(), split_tokens.end(), str) != split_tokens.end()) {
        new_vector.push_back(str);
        return new_vector;
    } else {
        for (auto & x: split_tokens) {
            position = str.find(x);
            while (position != std::string::npos) {
                if (position == 0) {
                    token = str.substr(0, position + x.length());
                    next = str.substr(position + x.length(), std::string::npos);
                    new_vector.push_back(token);
                    next_vector = split_by_token(next);
                    new_vector.insert(new_vector.end(), next_vector.begin(), next_vector.end());
                    return new_vector;
                } else if (position == str.length() - x.length()) {
                    token = str.substr(0, position);
                    next = str.substr(position, std::string::npos);
                    new_vector = split_by_token(token);
                    next_vector.push_back(next);
                    new_vector.insert(new_vector.end(), next_vector.begin(), next_vector.end());
                    return new_vector;
                } else {
                    token = str.substr(0, position);
                    next = str.substr(position + 1, std::string::npos);
                    new_vector = split_by_token(token);
                    new_vector.push_back(x);
                    next_vector = split_by_token(next);
                    new_vector.insert(new_vector.end(), next_vector.begin(), next_vector.end());
                    return new_vector;
                }
            }
        }
    }
    new_vector.push_back(str);
    return new_vector;
}

void Parser::create_iterators()
{
    token_begin = token_vector.begin();
    token_end = token_vector.end();
    line_begin = line_number_vector.begin();
    line_end = line_number_vector.end();
}


// Scanner.cpp
Scanner::Scanner(std::string file_name)
        : Parser(std::move(file_name)) {
    iterate_through_tokens();
    output_to_file();
}

void Scanner::output_to_file() {
    std::ofstream outfile("hw3out.txt");
    for (int i = 0; i < token_vector.size(); ++i) {
        outfile << token_code_vector[i] << "\t" << token_vector[i] << std::endl;
    }
};

bool Scanner::is_numeric_constant(std::string token) {
    return !token.empty() && std::find_if(
            token.begin(),
            token.end(), [](char c) { return !std::isdigit(c); }) == token.end();
}

bool Scanner::is_keyword(std::string token)
{
    return std::find(keywords.begin(), keywords.end(), token) != keywords.end();
}
bool Scanner::is_operator(std::string token)
{
    return operators.find(token) != operators.end();
}
bool Scanner::is_delimiter(std::string token)
{
    return delimiters.find(token) != delimiters.end();
}
void Scanner::iterate_through_tokens()
{
    for (auto& x: token_vector)
    {
        if (is_keyword(x))
        {
            token_code_vector.push_back(1);
            continue;
        }
        if (is_operator(x))
        {
            token_code_vector.push_back(3);
            continue;
        }
        if (is_numeric_constant(x))
        {
            token_code_vector.push_back(5);
            continue;
        }
        if (is_delimiter(x))
        {
            token_code_vector.push_back(4);
            continue;
        }
        else            // Assume is_identifier(x) == true;
        {
            token_code_vector.push_back(2);
            continue;
        }
    }

}

// main.cpp
int main()
{
    Scanner a("hw3in.txt");
}