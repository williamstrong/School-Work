//
//  EulerCircuit.cpp
//  NetworkGraphs
//
//  Created by William Strong on 10/4/17.
//  Copyright © 2017 William Strong. All rights reserved.
//

#include "stdafx.h"
#include "EulerCircuit.hpp"

std::vector<std::string> store_in_vector(json data){
    std::vector<std::string> vector;
    for (auto& element: data) {
        vector.push_back(element);
    }
    return vector;
}
MapVector store_in_map(json data){
    std::map<std::string, std::vector<std::string>> map;
    for (json::iterator it = data.begin(); it != data.end(); ++it) {
        map.emplace(std::make_pair(it.key(), store_in_vector(*it)));
    }
    return map;
}
//std::vector<std::string> find_a_circuit(json data){
//    json tmp_data = data;
//    for (json::iterator it = data.begin(); it != data.end() ; ++it) {

//    }
//}
bool is_empty(int edges, int vertices) {
    if (!edges || !vertices) return false;
    else return true;
}

// Display total number of edges in the map.
int edges(json data) {
    int edges;
    int tmp = 0;
    for (auto& i: data) {
        tmp += (i).size();
    }
    edges = tmp/2;
    //std::cout << edges << std::endl;
    return edges;
}
// Total number of vertices.
int number_of_vertices(json data)
{
	return (int)data.size();
}
void delete_edge(MapVector& data, std::string first, std::string second) {

	if (std::find(data[first].begin(), data[first].end(), second) != data[first].end()) {
		data[first].erase(std::find(data[first].begin(), data[first].end(), second));
	}
	if (std::find(data[second].begin(), data[second].end(), first) != data[second].end()) {
		data[second].erase(std::find(data[second].begin(), data[second].end(), first));
	}

}
std::vector<std::string> get_one_circuit(MapVector data, std::string start) {
	MapVector tmp_map = data;
	std::vector<std::string> result_vector;
	std::string current_vertex;
	std::string next_vertex;
	result_vector.push_back(start);

	current_vertex = start;

	while (tmp_map[current_vertex].empty() == false) {
		next_vertex = tmp_map[current_vertex].back();
		result_vector.push_back(next_vertex);
		delete_edge(tmp_map, current_vertex, next_vertex);
		current_vertex = next_vertex;
	}
	if (current_vertex != start) {
		std::vector<std::string> empty(NULL);
		return empty;
	}
	else {
		return result_vector;
	}

}

// This is prototype for modular implementation
std::vector<std::string> stitch_in_new_circuit(std::vector<std::string> current_path, MapVector new_map);

// Test implementation
void stitch_in_new_circuit(std::vector<std::string>& current_path) {
	MapVector new_map;
    std::string common_pt = "v4";
	std::string new_1 = "v6";
	std::string new_2 = "v7";
    std::vector<std::string> common_vec({"v6", "v7"});
	std::vector<std::string> newv_1({ "v4", "v7" });
	std::vector<std::string> newv_2({ "v4", "v6" });
    new_map[common_pt] = common_vec;
	new_map[new_1] = newv_1;
	new_map[new_2] = newv_2;

	std::vector<std::string> new_path;

	std::string stitch_point;

    for (auto& x: new_map) {
        if (std::find(current_path.begin(), current_path.end(), x.first) != current_path.end()) {
            stitch_point = x.first;
            new_path = get_one_circuit(new_map, stitch_point);
            break;
        }
    }

	// Find stitch point and stitch
	for (auto it = current_path.begin(); it != current_path.end(); ++it) {
		if (std::find(new_path.begin(), new_path.end(), *it) != new_path.end()) {
            *it = new_path[0];
			current_path.insert(it+1, new_path.begin()+1, new_path.end());
            break;
		}
	}
}

// Prototype for final function
void find_local_bridge(MapVector data);
void find_local_bridge() {
    // Make test map
    std::vector<std::string> test_string({"v1","v2","v3","v4","v5","v6"});
    std::vector<std::vector<std::string>> test_vects;
    test_vects.push_back({"v2","v5","v6"});
    test_vects.push_back({"v1","v3","v4"});
    test_vects.push_back({"v2"});
    test_vects.push_back({"v2"});
    test_vects.push_back({"v1"});
    test_vects.push_back({"v1"});

    MapVector test_map;

    for (int i = 0; i < 6; ++i) {
        test_map[test_string[i]] = test_vects[i];
    }

    for (auto& x: test_map) {

    }
}

bool find_euler_circuit(MapVector data, int edges, int vertices) {
	if (edges == 0 || vertices == 0) return false;

	std::string starting_vertex = data.begin()->first;

	std::vector<std::string> result_vect = get_one_circuit(data, starting_vertex);
	std::cout << "A Eulerian path from the file \"data.json\" " << std::endl;
	for (auto& x : result_vect) {
		std::cout << x << std::endl;
	}
	std::cout << "Stitching an extension off of V4" << std::endl << "Here is the new circuit:" << std::endl;
	stitch_in_new_circuit(result_vect);
	for (auto& x : result_vect) {
		std::cout << x << std::endl;
	}

}
// Read file and create json representation.
bool read_file(std::string filename) {
    json j;

    std::ifstream file(filename);
    if (!file) return false;

    file >> j;

    int num = number_of_vertices(j);

//    std::cout << num <<std::endl;
    int e = edges(j);
//    for (auto& element: j) {
//        std::cout << element << std::endl;
//    }

    MapVector v = store_in_map(j);
	find_euler_circuit(v, e, num);

    return true;

}

