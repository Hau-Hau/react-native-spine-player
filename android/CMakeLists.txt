project("react-native-spine-player")
cmake_minimum_required(VERSION 3.13)

set(CMAKE_VERBOSE_MAKEFILE on)
set (CMAKE_CXX_STANDARD 17)
set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DSK_GL -DSK_GANESH -DSK_BUILD_FOR_ANDROID -DFOLLY_NO_CONFIG=1 -DFOLLY_HAVE_CLOCK_GETTIME=1 -DFOLLY_HAVE_MEMRCHR=1 -DFOLLY_USE_LIBCPP=1 -DFOLLY_MOBILE=1 -DON_ANDROID -DONANDROID")

set (PACKAGE_NAME "react-native-spine-player")

link_directories(${CMAKE_CURRENT_LIST_DIR}/../libs/android/${ANDROID_ABI})

# Add spine-cpp library
set(SPINE_RUNTIMES_PATH ${CMAKE_CURRENT_LIST_DIR}/../cpp/spine-cpp)
file(GLOB SPINE_CPP_HEADERS "${SPINE_RUNTIMES_PATH}/include/**/*.h")
file(GLOB SPINE_CPP_SOURCES "${SPINE_RUNTIMES_PATH}/src/**/*.cpp")
include_directories(${SPINE_RUNTIMES_PATH}/include)
add_library(spine-cpp STATIC ${SPINE_CPP_SOURCES} ${SPINE_CPP_HEADERS})

# Add SpinePlayerView library
set(RTN_SPINE_COMPONENT_PATH ${CMAKE_CURRENT_LIST_DIR}/src/main/cpp)
file(GLOB RTN_SPINE_COMPONENT_HEADERS "${RTN_SPINE_COMPONENT_PATH}/*.h" "${RTN_SPINE_COMPONENT_PATH}/**/*.h")
file(GLOB RTN_SPINE_COMPONENT_SOURCES "${RTN_SPINE_COMPONENT_PATH}/*.cpp" "${RTN_SPINE_COMPONENT_PATH}/**/*.cpp")
add_library(${PACKAGE_NAME} SHARED ${RTN_SPINE_COMPONENT_SOURCES} ${RTN_SPINE_COMPONENT_HEADERS})

# Add skia library
set(SKIA_HEADERS_PATH ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia)
set(SKIA_COMPILED_PATH ${CMAKE_CURRENT_LIST_DIR}/../libs/android/${ANDROID_ABI})
file(GLOB LIBRN_DIR ${SKIA_COMPILED_PATTH})
target_include_directories(
        ${PACKAGE_NAME}
        PRIVATE
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/config/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/core/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/effects/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/utils/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/pathops/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/modules/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/modules/skparagraph/include/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia/include/
        ${CMAKE_CURRENT_LIST_DIR}/../cpp/skia
)
add_library(skia STATIC IMPORTED)
set_property(TARGET skia PROPERTY IMPORTED_LOCATION "${SKIA_COMPILED_PATH}/libskia.a")

add_compile_options(
    -frtti
    -std=c++17
    -Wno-gnu-zero-variadic-macro-arguments)

target_link_libraries(
        ${PACKAGE_NAME}
        skia
        spine-cpp
        log
        -ljnigraphics
        -lGLESv2
        -lEGL
        -landroid)
    