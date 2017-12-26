
//#include <cstddef>
#include <vector>

class large_int
{
public:
    large_int() = default;
    explicit large_int(int);
    large_int(const large_int&) = default;
    explicit large_int(std::vector<int>);
    ~large_int() = default;

    large_int& operator=(int);
    large_int& operator=(const large_int &);
    large_int& operator+=(const large_int&);
    large_int operator+(const large_int&) const;
    large_int& operator-=(const large_int&);
    large_int operator-(const large_int&) const;
    large_int& operator*=(const large_int&);
    large_int operator*(const large_int&) const;

    friend std::ostream& operator<<(std::ostream& , const large_int&);

    unsigned long size() const;



//private:
    std::vector<int> number;



};

int length(int num);
std::vector<int> i_to_vec(int num);
large_int pow10(large_int n, int m);
int vec_to_i(large_int n);
int norm_mult(large_int x, large_int y);
large_int divide10(large_int, int);
large_int rem_divide10(large_int, int);
bool is_zero(large_int);
bool test(int, int);