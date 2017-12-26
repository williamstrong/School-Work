//
//  mergesort.cpp
//  mergesort
//
//  Created by William Strong on 10/5/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include "mergesort.hpp"


bool sort(std::string file) {
    
    std::ifstream sort_file (file);
    int length_of_file = length_of_array(sort_file);
    int numbers[length_of_file];
    reset_file(sort_file);
    if(read_file_into_array(sort_file, numbers)){
        // Thread mergesort by calling mergesort with the first half in one thread, the second half in another,
        // and merge the third.

        mergesort(numbers, 0, (length_of_file - 1));
        write_array_into_file(file, numbers, length_of_file);
        
        return true;
    }
    else {
        return false;
    }
}

void write_array_into_file(std::string file_name, int* array, int length)
{
    std::string out_file = file_name.append("_sorted");
    std::ofstream file(out_file);
    
    for (int i = 0; i < length; i++) {
        file << array[i];
        file << "\n";
    }
}

void reset_file(std::ifstream& file)
{
    file.clear();
    file.seekg(0, std::ios::beg);
}

bool read_file_into_array(std::ifstream& file, int* list) {
    std::string line;
    int index = 0;
    int int_line;
    
    if (is_empty(file) == 1) {
        std::cout << "File is empty. Please choose another file." << std::endl;
        return false;
    }
    
    while (std::getline(file, line)) {
        int_line = convert_string_to_int(line);
        list[index] = int_line;
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

int length_of_array(std::ifstream& file) {
    std::string line;
    int count = 0;
    
    while (std::getline(file, line)) {
        ++count;
    }
    return count;
}

void mergesort(int* list, int low, int high) {
    if (low < high) {
        int mid = (low + high)/2;
        mergesort(list, low, mid);
        mergesort(list, mid+1, high);
        merge(list, low, mid, high);
    }
}

void merge(int* list, int low, int mid, int high) {
    int n1 = mid - low + 1;
    int n2 = high - mid;
    int* low_half;
    int* high_half;
    
    low_half = new int[n1 + 1];
    high_half = new int[n2 + 1];
    
    int i, j;

    for (i = 0; i < n1; i++) {
        low_half[i] = list[low + i];
    }
    low_half[n1] = std::numeric_limits<int>::max();
    for (i = 0; i < n2; i++) {
        high_half[i] = list[(mid+1) + i];
    }
    high_half[n2] = std::numeric_limits<int>::max();
    i = 0;
    j = 0;
    
    for (int k = low; k < high + 1; k++) {
        if (low_half[i] <= high_half[j]) {
            list[k] = low_half[i];
            i++;
        }
        else {
            list[k] = high_half[j];
            j++;
        }
    }
    
    //    Cleanup
    delete low_half;
    delete high_half;
}
