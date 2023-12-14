import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")


def part1(L):
    width = len(L[0])
    result = 0
    for col in range(width):
        # construct the line so it's easier to deal with
        line = ""
        for row in range(len(L)):
            line += L[row][col]

        # shift the line
        line = shift_line(line)

        # fix the original map.
        for row in range(len(L)):
            L[row] = L[row][:col] + line[row] + L[row][col + 1 :]

    rank = len(L)
    for line in L:
        result += rank * line.count("O")
        rank -= 1

    print(result)


def shift_line(line):
    l = 0
    r = 0
    while l < len(line):
        if line[l] != ".":
            l += 1
            r += 1
            continue

        while True:
            r += 1
            if r >= len(line):
                return line

            if line[r] == ".":
                continue

            if line[r] == "#":
                r += 1
                l = r
                break

            line = line[:l] + "O" + line[l + 1 :]
            line = line[:r] + "." + line[r + 1 :]
            l += 1
            r = l
            break

    return line


part1(L)
