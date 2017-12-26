//
//  EulerCircuit.hpp
//  NetworkGraphs
//
//  Created by William Strong on 10/4/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#ifndef EulerCircuit_hpp
#define EulerCircuit_hpp

#include "stdafx.h"

typedef std::map<std::string, std::vector<std::string>> MapVector;

std::vector<std::string> store_in_vector(json data);
MapVector store_in_map(json data);
std::vector<std::string> find_a_circuit(json data);
bool read_file(std::string filename);
int number_of_vertices(json data);
int edges(json data);
bool is_empty(int, int);

#endif