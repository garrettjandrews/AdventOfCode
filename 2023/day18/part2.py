import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")

steps = []
directions = {"0": (0, 1), "2": (0, -1), "3": (-1, 0), "1": (1, 0)}
for line in L:
    direction, length, color = line.split(" ")
    direction = color[-2]
    length = color[-7:-2]

    steps.append((direction, int(length, 16)))

row = 0
col = 0
perimeter = 0
area = 0

for step in steps:
    direction, length = step
    colStep, rowStep = directions[direction]
    colStep, rowStep = colStep * length, rowStep * length
    row += rowStep
    col += colStep

    perimeter += length
    area += row * colStep

print(area + perimeter // 2 + 1)
