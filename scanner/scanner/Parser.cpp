#include "stdafx.h"
#include "Parser.h"


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
