#include "stdafx.h"

#include "IO.h"
#include <iostream>

input::input()
{
	cin_prompt();
}

input::input(const std::string input_file) : post_fix(file_into_vector(open_file(input_file))) {}

bool input::open_file(const std::string file_name)
{
	file.open(file_name);
	if (!file)
	{
		std::cout << "File is empty. Use std::cin instead." << std::endl;
		return false;
	}
	return true;
}

void input::turn_string_into_char_vect(const std::string tmp_str)
{
	for (auto& x : tmp_str)
	{
		if (x != ' ')
		{
			input_vector.push_back(x);
		}
	}
}

std::vector<char> input::file_into_vector(const bool b)
{
	if (b) {
		std::string tmp_str;
		std::getline(file, tmp_str);
		turn_string_into_char_vect(tmp_str);
	} else
	{
		// use std::cin
		cin_prompt();
	}
	return input_vector;
}

void input::cin_prompt()
{
	std::string tmp_str;
	std::string result;

	while (true)
	{
		std::cout << "Enter an infix expression: ";
		std::cin.clear();

		std::getline(std::cin, tmp_str);
		turn_string_into_char_vect(tmp_str);

		post_fix post(input_vector);
		if (post.accepted)
		{
			for (auto& x : post.postfix)
			{
				result.push_back(x);
				result.push_back(' ');
			}
			std::cout << "The postfix notation of the expression is " << result << " and the expression is accepted!" << std::endl;
		}
		else
		{
			std::cout << "The infix notation is incorrect. Uneven parenthesis" << std::endl;
		}
		std::cout << "More expressions: ";
		std::cin.clear();
		std::string cont;
		std::getline(std::cin, cont);
		if (cont == "y")
		{
			// continue loop
			result.clear();
			post.clear();
			input_vector.clear();
			continue;
		}
		else if (cont == "n")
		{
			// break loop
			break;
		}
		else
		{
			// bad input
			std::cout << "Bad input... you broke me." << std::endl;
		}
	}
}
