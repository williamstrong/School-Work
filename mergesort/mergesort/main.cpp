//
//  main.cpp
//  mergesort
//
//  Created by William Strong on 10/3/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <new>

#include "mergesort.hpp"
#include "testing.hpp"

int main(int argc, const char * argv[]) {
    
    if (argc == 1) {
        std::cout << "Use \"mergesort <filename>\" to get the numbers in <filename> sorted in the file <filename>_sorted." << std::endl;
        return 0;
    }

    if(std::strcmp(argv[1], "test") == 0)
    {
        for (int i = 0; i < (argc - 2); i++) {
            if (test(argv[i + 2])) {
                std::cout << argv[i+2] << " is being sorted correctly by your mergesort algorithm." << std::endl;
            }
            else {
                std::cout << "Your mergesort algorithm is not working correctly." << std::endl;
            }
        }
    }
    
    
    else {
        if (!std::strcmp(argv[1], "test")) {
            std::cout << "You may have meant to meant to test the program or have used a file named \"test\"." << std::endl;
        }
        else {
            for (int i = 0; i < (argc - 1); i++) {
                if (sort(argv[i + 1]) == true) {
                    std::cout << argv[i + 1] << " is now sorted in " << argv[i+1] << "_sorted." << std::endl << std::endl;
                }
                else {
                    std::cout << "There was an error with your file." << std::endl << std::endl;
                }
            }
        }
    }
    

    
    
    
//    sort("list.txt");
    
    return 0;
}


