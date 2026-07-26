cmake_minimum_required(VERSION 3.22.1)

project("kidsdraw_native")

add_library(
        kidsdraw_native
        SHARED
        drawing_engine.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        kidsdraw_native
        ${log-lib})
