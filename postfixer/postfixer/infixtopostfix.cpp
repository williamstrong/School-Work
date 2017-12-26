#include "stdafx.h"

#include "infixtopostfix.h"

void post_fix::clear()
{
	postfix.clear();
	infix.clear();
	operators.clear();
}


bool post_fix::check_if_valid()
{
	accepted = (std::count(infix.begin(), infix.end(), '(') == std::count(infix.begin(), infix.end(), ')'));
	return accepted;
}
void post_fix::if_to_pf()
{
	if (!check_if_valid()) return;
	for (auto& token : infix)
	{
		if (check_operator(token))
		{
			handle_operator(token);
		}
		else
		{
			postfix.push_back(token);
		}
	}
	for (auto& token : operators)
	{
		op_to_pf();
	}
	return;
}

post_fix::post_fix(std::vector<char> input)
{
	infix = input;
	if_to_pf();
}

bool post_fix::check_operator(const char token)
{
	return (token == '*' || token == '+' || token == '(' || token == ')');
}

bool post_fix::can_push_back(const char token)
{
	if (operators.empty())
	{
		return true;
	} else
	{
		return operators.back() == '(' || token == '(';
	}
}

void post_fix::handle_operator(const char token)
{
	// push_back true
	if (can_push_back(token))
	{
		operators.push_back(token);
		return;
	}
	if (tok_non_par(token)) {
		if (!add_by_precedence(token))
		{
			operators.push_back(token);
			return;
		}
	}
	if (token == ')')
	{
		// pop ')'
		while (operators.back() != '(')
		{
			// save last operator to postfix and pop operators
			op_to_pf();
		}
		// pop '('
		operators.pop_back();
	}
}

bool post_fix::tok_non_par(const char token)
{
	return (token == '*' || token == '+');
}

bool post_fix::opback_non_par(const char token)
{
	if (!operators.empty())
	{
		return (operators.back() == '*' || operators.back() == '+');
	}
	else return false;
}

bool post_fix::add_by_precedence(const char token)
{
	while (opback_non_par(token))
	{
		if (precedence(token))
		{
			operators.push_back(token);
			return true;
		}
		else
		{
			op_to_pf();
			bool a = add_by_precedence(token);
			return a;
		}
	}
	return false;
}


bool post_fix::precedence(const char token)
{
	// Returns false if token precedence is lower than top of stack.
	return (operators.back() == '+' && token == '*');
}

void post_fix::op_to_pf()
{
	postfix.push_back(operators.back());
	operators.pop_back();
}