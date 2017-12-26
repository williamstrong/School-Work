#pragma once

#include <string>
#include <vector>
#include <fstream>
#include <set>


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

