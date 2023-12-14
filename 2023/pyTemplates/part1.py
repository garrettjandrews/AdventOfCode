import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")

print(L)
