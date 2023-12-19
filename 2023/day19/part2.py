import sys

input_filepath = sys.argv[1]

lines = open(input_filepath).read().strip().split("\n")
L = []
i = 0
while len(lines[i]) > 0:
    L.append(lines[i])
    i += 1


# for each line, figure out the range of values that can lead to an A
