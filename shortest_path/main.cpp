#include <iostream>
#include <vector>
#include <fstream>
#include <sstream>

// Header
void read_file(const std::string&);


std::vector<std::vector<int>> P;
std::vector<std::vector<int>> D;
int N;


std::vector<std::vector<int>> C;
std::vector<int> path_vec;

void floyd() {
    int i, j, k;
    for (i = 0; i < N; ++i) {
        for (j = 0; j < N; ++j) {
            D[i][j] = C[i][j];
//            P[i][j] = -1;
        }
        D[i][i] = 0;
    }
    for (k = 0; k < N; ++k) {
        for (i = 0; i < N; ++i) {
            for (j = 0; j < N; ++j) {
                if (D[i][k] + D[k][j] < D[i][j]) {
                    D[i][j] = D[i][k] + D[k][j];
                    P[i][j] = k+1;
                }
            }
        }
    }
}
void path(int a, int b) {
    int k;

    k = P[a-1][b-1];
    if (k == 0) return;
    path_vec.push_back(k);
    path(a, k);
}
void tot_path(int a, int b) {
    path_vec.clear();
    path_vec.push_back(b);
    path(a,b);
    path_vec.push_back(a);
}


void read_file(const std::string& file_name) {
    std::string in;
    std::ifstream file(file_name);
    std::istringstream iss;
    std::string tmp_str;
    std::vector<int> tmp_vec;
    while (getline(file, in)) {
        iss.str(in);
        while (iss >> tmp_str) {
            tmp_vec.push_back(std::stoi(tmp_str));
        }
        C.push_back(tmp_vec);
        iss.clear();
        tmp_vec.clear();
    }

    // Set variables
    N = static_cast<int>(C.size());

    std::vector<std::vector<int>> t(N, std::vector<int> (N, 0));
    P = t;
    D = t;

}

template <class T>
void print_mat(T t) {
    for (auto x: t) {
        for (auto y: x){
            std::cout << y << " ";
        }
        std::cout << std::endl;
    }
}
template <class T>
void print_path(T t) {
    for (auto i = t.rbegin(); i < t.rend(); ++i) {
        std::cout << *i << " ";
    }
    std::cout << std::endl;
}

int main() {
    std::string file = "hw7in.txt";

    read_file(file);
    floyd();

    print_mat(D);
    std::cout << std::endl;
    print_mat(P);
    std::cout << std::endl;


    std::string in;
    std::istringstream iss;
    std::string a, b;
    int ia, ib;
    while (true) {

        std::cout << "Enter a source vertex and a destination vertex: ";
        std::cout.flush();

        std::getline(std::cin, in);
        iss.str(in);
        iss >> a; iss >> b;
        iss.clear();
        ia = std::stoi(a);
        ib = std::stoi(b);
        tot_path(ia, ib);
        print_path(path_vec);

        while (true) {
            std::cout << "More expressions? (y/n): ";
            std::cout.flush();
            std::getline(std::cin, in);
            if (in == "n") return 0;
            else if (in == "y") break;
            else continue;
        }
    }
}