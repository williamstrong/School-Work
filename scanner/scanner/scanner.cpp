// scanner.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "scanner.h"

//using Parser::Parser;

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


