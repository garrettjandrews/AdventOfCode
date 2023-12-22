import sys
import math

filepath = sys.argv[1]

L = open(filepath).read().strip().split("\n")


bricks = []
for line in L:
    top, bot = line.split("~")
    top = top.split(",")
    bot = bot.split(",")
    brick = []
    for i in range(len(top)):
        x1 = min(int(top[i]), int(bot[i]))
        x2 = max(int(top[i]), int(bot[i]))

        x = []
        for j in range(x1, x2 + 1):
            x.append(j)

        brick.append(x)

    bricks.append(brick)


def getXYCoordinates(brick):
    res = set()
    for x in brick[0]:
        for y in brick[1]:
            res.add((x, y))

    return res


def getZ(brick):
    return min(brick[2])


def brickUnder(brick):
    global bricks
    z = getZ(brick)
    if z == 1:
        return False

    xy = getXYCoordinates(brick)

    poss_bricks = [x for x in bricks if max(x[2]) == z - 1]
    for pb in poss_bricks:
        for coord in getXYCoordinates(pb):
            if coord in xy:
                return True

    return False


maxZ = 1
for brick in bricks:
    maxZ = max(maxZ, getZ(brick))

# gonna build up XY coords by level of Z
bricks = sorted(bricks, key=lambda brick: getZ(brick))

for brick in bricks:
    # shift em down
    while getZ(brick) > 1 and not brickUnder(brick):
        for z in range(len(brick[2])):
            brick[2][z] -= 1

for brick in bricks:
    print(brick)
