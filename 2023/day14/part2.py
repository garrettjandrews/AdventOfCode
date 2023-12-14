import sys

input_filepath = sys.argv[1]

L = open(input_filepath).read().strip().split("\n")


def cycle(L):
    width = len(L[0])
    for col in range(width):
        # construct the line so it's easier to deal with
        line = ""
        for row in range(len(L)):
            line += L[row][col]

        # shift the line
        line = shift_up(line)

        # fix the original map.
        for row in range(len(L)):
            L[row] = L[row][:col] + line[row] + L[row][col + 1 :]

    # go left
    for row in range(len(L)):
        L[row] = shift_left(L[row])

    # go down
    for col in range(width):
        # construct the line so it's easier to deal with
        line = ""
        for row in range(len(L)):
            line += L[row][col]

        # shift the line
        line = shift_down(line)

        # fix the original map.
        for row in range(len(L)):
            L[row] = L[row][:col] + line[row] + L[row][col + 1 :]

    # go right
    for row in range(len(L)):
        L[row] = shift_right(L[row])

    return L


def shift_up(line):
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


def shift_left(line):
    return shift_up(line)


def shift_down(line):
    return shift_up(line[::-1])[::-1]


def shift_right(line):
    return shift_down(line)


def calc_result(L):
    rank = len(L)
    result = 0
    for line in L:
        result += rank * line.count("O")
        rank -= 1

    return result


originalL = [row[:] for row in L]
i = 1
memo = [originalL]
while True:
    # print("Cycle " + str(i))
    L = cycle(L)
    if L in memo:
        break
    memo.append(L.copy())
    i += 1


print(f"Cycle found at {i}")
cycles_needed = memo.index(L) + ((1_000_000_000 - memo.index(L)) % (i - memo.index(L)))


for i in range(cycles_needed):
    originalL = cycle(originalL)

print(calc_result(originalL))
