#pragma once

#include <fstream>
#include <string>
#include <vector>

#include "infixtopostfix.h"

class input : public post_fix
{

public:
	input();
	explicit input(const std::string input_file);
	~input() = default;
	void cin_prompt();

private:
	std::vector<char> file_into_vector(const bool);
	bool open_file(const std::string);
	void input::turn_string_into_char_vect(const std::string tmp_str);


	std::vector<char> input_vector;
	std::ifstream file;
};

template <class Base>
class output : public Base
{
	template <class T>
	output(T);
	~output();
};