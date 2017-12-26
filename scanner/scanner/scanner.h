#pragma once

#include "stdafx.h"
#include "Parser.h"


/*
keyword 1
identifier 2
operator 3
delimiter 4
numeric constant 5
*/

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