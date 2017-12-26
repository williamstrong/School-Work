//
//  testing.cpp
//  mergesort
//
//  Created by William Strong on 10/6/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include "testing.hpp"


bool test(std::string file)
{
    sort(file);
    
    std::ifstream unsorted(file);
    
    std::string sorted_file = file.append("_sorted");
    std::ifstream sorted(sorted_file);
    
    int length = length_of_array(sorted);
    
    int unsorted_array[length];
    read_file_into_array(unsorted, unsorted_array);

    
    reset_file(sorted);
    
    int sorted_array[length];
    read_file_into_array(sorted, sorted_array);


    
    std::sort(unsorted_array, unsorted_array+length);
    
    bool sorting_passes = true;
    
    return sorting_passes;
    
}
