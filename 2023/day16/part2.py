import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")

unvis = set()
for row in range(len(L)):
    for col in range(len(L[0])):
        unvis.add((row, col))


class Beam:
    def __init__(self, row, col, dir, curr):
        self.row = row
        self.col = col
        self.dir = dir
        self.curr = curr

    def next(self):
        left = Beam(self.row, self.col - 1, "l", "?")
        right = Beam(self.row, self.col + 1, "r", "?")
        up = Beam(self.row - 1, self.col, "u", "?")
        down = Beam(self.row + 1, self.col, "d", "?")
        if self.curr == ".":
            if self.dir == "r":
                return [right]
            if self.dir == "l":
                return [left]
            if self.dir == "u":
                return [up]
            if self.dir == "d":
                return [down]

        if self.curr == "-":
            if self.dir == "r":
                return [right]
            if self.dir == "l":
                return [left]
            if self.dir in ["u", "d"]:
                return [right, left]

        if self.curr == "|":
            if self.dir == "u":
                return [up]
            if self.dir == "d":
                return [down]
            if self.dir in ["r", "l"]:
                return [up, down]

        if self.curr == "\\":
            if self.dir == "r":
                return [down]
            if self.dir == "l":
                return [up]
            if self.dir == "u":
                return [left]
            if self.dir == "d":
                return [right]

        if self.curr == "/":
            if self.dir == "r":
                return [up]
            if self.dir == "l":
                return [down]
            if self.dir == "u":
                return [right]
            if self.dir == "d":
                return [left]

        return []


final_res = -1
for row in range(len(L)):
    starting_point = Beam(row, 0, "r", L[row][0])
    beams = [starting_point]
    vis = set()
    vis_coords = set()

    while len(beams) > 0:
        beam = beams.pop()
        vis.add((beam.row, beam.col, beam.dir, beam.curr))
        vis_coords.add((beam.row, beam.col))

        # figure out what beams to add
        next_beams = beam.next()
        for nbeam in next_beams:
            if (nbeam.row, nbeam.col) in unvis:
                new_curr = L[nbeam.row][nbeam.col]
                nbeam.curr = new_curr
                if (nbeam.row, nbeam.col, nbeam.dir, nbeam.curr) not in vis:
                    beams.append(nbeam)

    final_res = max(len(vis_coords), final_res)


for col in range(len(L[0])):
    starting_point = Beam(0, col, "d", L[0][col])
    beams = [starting_point]
    vis = set()
    vis_coords = set()

    while len(beams) > 0:
        beam = beams.pop()
        vis.add((beam.row, beam.col, beam.dir, beam.curr))
        vis_coords.add((beam.row, beam.col))

        # figure out what beams to add
        next_beams = beam.next()
        for nbeam in next_beams:
            if (nbeam.row, nbeam.col) in unvis:
                new_curr = L[nbeam.row][nbeam.col]
                nbeam.curr = new_curr
                if (nbeam.row, nbeam.col, nbeam.dir, nbeam.curr) not in vis:
                    beams.append(nbeam)

    final_res = max(len(vis_coords), final_res)


for row in range(len(L)):
    starting_point = Beam(row, len(L[0]) - 1, "l", L[row][len(L[0]) - 1])
    beams = [starting_point]
    vis = set()
    vis_coords = set()

    while len(beams) > 0:
        beam = beams.pop()
        vis.add((beam.row, beam.col, beam.dir, beam.curr))
        vis_coords.add((beam.row, beam.col))

        # figure out what beams to add
        next_beams = beam.next()
        for nbeam in next_beams:
            if (nbeam.row, nbeam.col) in unvis:
                new_curr = L[nbeam.row][nbeam.col]
                nbeam.curr = new_curr
                if (nbeam.row, nbeam.col, nbeam.dir, nbeam.curr) not in vis:
                    beams.append(nbeam)

    final_res = max(len(vis_coords), final_res)


for col in range(len(L[0])):
    starting_point = Beam(len(L) - 1, col, "u", L[len(L) - 1][col])
    beams = [starting_point]
    vis = set()
    vis_coords = set()

    while len(beams) > 0:
        beam = beams.pop()
        vis.add((beam.row, beam.col, beam.dir, beam.curr))
        vis_coords.add((beam.row, beam.col))

        # figure out what beams to add
        next_beams = beam.next()
        for nbeam in next_beams:
            if (nbeam.row, nbeam.col) in unvis:
                new_curr = L[nbeam.row][nbeam.col]
                nbeam.curr = new_curr
                if (nbeam.row, nbeam.col, nbeam.dir, nbeam.curr) not in vis:
                    beams.append(nbeam)

    final_res = max(len(vis_coords), final_res)


print(final_res)
