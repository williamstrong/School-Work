//
// Created by William Strong on 10/30/17.
//

#include <algorithm>
#include <vector>
#include <iostream>
#include <cmath>
#include "largeintarithmetic.h"

large_int::large_int(int num) {
    number = i_to_vec(num);
}
large_int::large_int(const std::vector<int> int_vec) : number(int_vec) {}

large_int& large_int::operator=(int num) {
    number = i_to_vec(num);
    return *this;
}
large_int& large_int::operator=(const large_int &obj) {
    if (this != &obj) {
        // create the obj;
        number = obj.number;
    }
    return *this;
}
template <class A, class B>
int max(A a, B b)
{
	return std::max(static_cast<int>(a), static_cast<int>(b));
}
template <class A, class B>
int min(A a, B b)
{
	return std::min(static_cast<int>(a), static_cast<int>(b));
}
template <class A, class B>
bool a_gr_b(A a, B b)
{
	return a > b;
}
large_int large_int::operator+(const large_int& num_add) const {
    large_int ret = *this;
    ret += num_add;
    return ret;
}
large_int& large_int::operator+=(const large_int& num_add) {


    auto n = max(number.size(), num_add.size());
	auto m = min(number.size(), num_add.size());
	// True if this is bigger than num_add
	auto th_bg = a_gr_b(number.size(), num_add.size());
	
    std::vector<int> tmp;

    int tmp_digit;
    int rem;
    bool next = false;

    for (auto i = 0; i < n; ++i) {

		if (i >= m)
		{
			if (th_bg) tmp_digit = number[i];
			else tmp_digit = num_add.number[i];
		} else tmp_digit = number[i] + num_add.number[i];
        if (next) {
            tmp_digit+=1;
            next = false;
        }
        rem = tmp_digit % 10;
        if (rem == 0 && tmp_digit != 0) {
            tmp_digit = 0;
            next = true;
        } else if ((tmp_digit / 10) > 0){
            tmp_digit = rem;
            next = true;
        }
        tmp.push_back(tmp_digit);
    }
    number = tmp;
    return *this;
}


large_int& large_int::operator-=(const large_int& num_sub) {
    auto n = max(number.size(), num_sub.size());
	auto m = min(number.size(), num_sub.size());
	// True if this is bigger than num_add
	auto th_bg = a_gr_b(number.size(), num_sub.size());

	std::vector<int> tmp;
    int tmp_digit;


    for (auto i = 0; i < n; ++i) {
		if (i >= m)
		{
			if (th_bg) tmp_digit = number[i];
			else tmp_digit = num_sub.number[i];
		}
		else tmp_digit = number[i] - num_sub.number[i];


        if (tmp_digit < 0) tmp_digit *= -1;
        tmp.push_back(tmp_digit);
    }
//    std::vector<int>::iterator num_end;
//    bool is_end = false;
//    auto i = tmp.begin();
//    while ( i < tmp.end()) {
//        std::cout << *i << std::endl;
//        if (*i != 0) {
//            is_end = false;
//        } else if (!is_end && *i == 0) {
//            is_end = true;
//            num_end = i;
//        }
//        ++i;
//    }
//    tmp.erase(num_end, tmp.end());
    number = tmp;
    return *this;
}
large_int large_int::operator-(const large_int& num_add) const {
    large_int ret = *this;
    ret -= num_add;
    return ret;
}

std::ostream& operator<<(std::ostream& os, const large_int& i) {
    for (auto j = i.number.rbegin(); j < i.number.rend(); ++j) {
        os << *j;
    }
    return os;
}

unsigned long large_int::size() const {
    return static_cast<unsigned long>(number.size());
}

int length(int num) {
    int ret_size = 0;
    while (num) {
        ++ret_size;
        num /= 10;
    }
    return ret_size;
}

large_int divide10(large_int num, const int pow) {
    std::vector<int> ret = num.number;
    ret.erase(ret.begin(), ret.begin()+pow);
    if (ret.empty()) ret.push_back(0);
    large_int ret_int(ret);
    return ret_int;

}
large_int rem_divide10(large_int num ,const int pow) {
    std::vector<int> ret = num.number;
    ret.erase(ret.begin()+pow, ret.end());
    if (ret.empty()) ret.push_back(0);
    large_int ret_int(ret);
    return ret_int;
}

large_int large_int::operator*(const large_int& num_add) const {
    large_int ret = *this;
    ret *= num_add;
    return ret;
}

std::vector<int> i_to_vec(int num) {
    std::vector<int> ret;
    int n = length(num);
    for (auto i = 0; i < n; ++i) {
        ret.push_back(num % 10);
        num /= 10;
    }
    return ret;
}

large_int& large_int::operator*=(const large_int& v) {
     large_int x, y, w, z, r, p, q;
    int n, m;

    n = max(number.size(), v.size());
    bool e  = is_zero(*this);
    bool g = is_zero(v);
    if (is_zero(*this) || is_zero(v)) {
        large_int zero_int(0);
        return zero_int;
    }
    else if (n <= 2) {
        large_int ret(i_to_vec(norm_mult(*this, v)));
		*this = ret;
    } // threshold is two. multiply numbers normally.
    else {
        m = n/2;
        // implement u divide 10^m and u rem 10^m
        x = divide10(*this, m);
        y = rem_divide10(*this, m);
        w = divide10(v,m);
        z = rem_divide10(v, m);
        r = (x+y) * (w+z);
        p = x * w;
        q = y * z;
        large_int test1 = pow10(p,2*m);
        large_int test2 = pow10((r - p - q),m);
        large_int test3 = r-p;
        test3-=q;
        test1 += test2;

        int t1 = vec_to_i(r) - vec_to_i(p);

        large_int ret;
        if (vec_to_i(r) > vec_to_i(p)) ret = pow10(p,2*m) + pow10((r - p - q),m) + q;
		*this = ret;
    }
	return *this;
}
large_int pow10(large_int n, int m) {
    for (auto i = 0; i < m; ++i) {
        n.number.insert(n.number.begin(), 0);
    }
    return n;
}

int vec_to_i(large_int n) {
    int power = 0;
    int ret = 0;
    for (auto x: n.number) {
        ret+=x*std::pow(10,power);
        ++power;
    }
    return ret;
}
int norm_mult(large_int x, large_int y) {
    int a, b;
    a = vec_to_i(x);
    b = vec_to_i(y);
    return a*b;
}
bool is_zero(large_int num) {
    return (num.size() == 0 && num.number.front() == 0);
}



