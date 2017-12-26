//
//  mergesort.hpp
//  mergesort
//
//  Created by William Strong on 10/5/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#ifndef mergesort_hpp
#define mergesort_hpp

#include <stdio.h>
#include <fstream>
#include <new>
#include <sstream>
#include <iostream>
#include <cmath>
#include <limits>

#endif /* mergesort_hpp */

bool sort(std::string);

bool read_file_into_array(std::ifstream&, int*);

void write_array_into_file(std::string,int*, int);

bool is_empty(std::ifstream&);

void reset_file(std::ifstream&);

int convert_string_to_int(std::string&);

int length_of_array(std::ifstream&);

void add_zeros(int, int*);

void mergesort(int*, int, int);      // Array, beggining, end

void merge(int*, int, int, int);    // Array, beginning, middle, end
