cmake_minimum_required(VERSION 3.22.1)
project(cart3r)

add_library(cart3r SHARED native-lib.cpp)

find_library(log-lib log)
target_link_libraries(cart3r ${log-lib})
