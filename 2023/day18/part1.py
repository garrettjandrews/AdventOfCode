import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")

steps = []
directions = {"R": (0, 1), "L": (0, -1), "U": (-1, 0), "D": (1, 0)}
for line in L:
    direction, length, color = line.split(" ")

    steps.append((direction, int(length)))

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
