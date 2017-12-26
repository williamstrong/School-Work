#pragma once


class post_fix
{
public:
	post_fix() = default;
	explicit post_fix(std::vector<char>);
	~post_fix() = default;
	void clear();

	std::vector<char> postfix;
	bool accepted;
private:
	std::vector<char> infix;
	std::vector<char> operators;

	bool check_if_valid();
	void if_to_pf();
	static bool check_operator(const char);
	void handle_operator(const char);
	void post_fix::op_to_pf();
	bool post_fix::precedence(const char token);
	bool post_fix::add_by_precedence(const char token);
	bool post_fix::can_push_back(const char token);
	static bool post_fix::tok_non_par(const char token);
	bool post_fix::opback_non_par(const char token);

};