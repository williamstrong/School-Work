//
// Created by William Strong on 10/30/17.
//

#include <iostream>
#include "largeintarithmetic.h"

int main() {
    large_int a, b, c, d, f;

    a = 111;
    b = 112;
    std::cout << a*b << std::endl;

    c = 871;
    d = 11635;
    f = c*d;
    std::cout << c*d << std::endl;

    return 0;
}