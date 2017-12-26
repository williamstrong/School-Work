//
//  mergesort.cpp
//  mergesort
//
//  Created by William Strong on 10/5/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include <stdio.h>
#include <fstream>
#include <sstream>
#include <iostream>
#include <string>
 
#include "quicksort.hpp"


void quicksort(int* num_array, int low, int high)
{
	if (low < high)
	{
		int p = partition(num_array, low, high);
		quicksort(num_array, low, (p - 1));
		quicksort(num_array, (p + 1), high);
	}
}

int partition (int * num_array, int low, int high)
{
	int pivot = num_array[high];
	int i = low - 1;

	for (auto j = low; j < high; ++j)
	{
		if (num_array[j] < pivot)
		{
			++i;
			std::swap(num_array[i], num_array[j]);
		}
	}
	if (num_array[high] < num_array[i + 1])
	{
		std::swap(num_array[i + 1], num_array[high]);
	}
	return i+1;
}

const int length_of_array(std::ifstream& file) {
	std::string line;
	int count = 0;

	while (std::getline(file, line)) {
		++count;
	}
	return count;
}

bool sort(const std::string file) {

	std::ifstream sort_file(file);
	const auto length_of_file = length_of_array(sort_file);
	int * numbers = new int[length_of_file];
	reset_file(sort_file);
	if (read_file_into_array(sort_file, numbers) == true) {
		quicksort(numbers, 0, (length_of_file - 1));
		write_array_into_file(file, numbers, length_of_file);

		return true;
	}
	else {
		return false;
	}
}

void write_array_into_file(std::string file_name, int * array, const int length)
{
    const auto out_file = file_name.insert(0, "sorted_");
    std::ofstream file(out_file);
    
	for (auto i = 0; i < length; i++) {
		file << array[i];
		file << "\n";
	}
}

void reset_file(std::ifstream& file)
{
	file.clear();
	file.seekg(0, std::ios::beg);
}

bool read_file_into_array(std::ifstream& file, int * list) {
	std::string line;
	int index = 0;
    
    if (is_empty(file) == 1) {
        std::cout << "File is empty. Please choose another file." << std::endl;
        return false;
    }
    
    while (std::getline(file, line)) {
        const auto int_line = convert_string_to_int(line);
		list[index] = int_line;
		std::cout << list[index] << std::endl;
    	++index;
    }


    
    return true;
}

bool is_empty(std::ifstream& pFile)
{
    return pFile.peek() == std::ifstream::traits_type::eof();
}

int convert_string_to_int(std::string& input)
{
    std::stringstream string_value(input);
    int value;
    string_value >> value;
    return value;
}


int main() {
	sort("hw5data.txt");
	return 0;
} 