import sys
import math
import queue

input_path = sys.argv[1]
L = open(input_path).read().strip().split("\n")
G = [[col for col in row] for row in L]

start = (len(G) - 1, len(G[0]) - 1)
end = (0, 0)

vis = set()
curr = start

while True:
    r, c = curr
    vis.add(curr)
    neighbors = []
    for cand_r, cand_c in [(r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)]:
        if cand_r in range(len(G)) and cand_c in range(len(G[0])):
            if (cand_r, cand_c) not in vis:
                neighbors.append((cand_r, cand_c))

    # for each neighbor, figure out which is the smallest and go there
    if len(neighbors) == 0:
        break

    min_dist = math.inf
    min_neighbor = [-1, -1]

    for neighbor in neighbors:
        neighbor_val = int(G[neighbor[0]][neighbor[1]])
        if neighbor_val < min_dist:
            min_dist = neighbor_val
            min_neighbor = neighbor

    curr = min_neighbor
    if curr == end:
        break

for row in range(len(G)):
    thisLine = ""
    for col in range(len(G[0])):
        if (row, col) in vis:
            thisLine += "X"
        else:
            thisLine += "."

    print(thisLine)
