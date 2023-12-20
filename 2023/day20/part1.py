import sys

input_filepath = sys.argv[1]
button_presses = sys.argv[2]

L = open(input_filepath).read().strip().split("\n")


class FlipFlop:
    def __init__(self, name):
        self.name = name
        self.state = "off"

    def handlePulse(self, pulse_type):
        if pulse_type != "low":
            return ""

        if self.state == "off":
            self.state = "on"
            return "high"

        self.state = "off"
        return "low"


class Conjunction:
    def __init__(self, name):
        self.name = name
        self.input_modules = {}

    def handlePulse(self, sender, pulse_type):
        self.input_modules[sender] = pulse_type
        for sender, pulse in self.input_modules.items():
            if pulse != "high":
                return "high"

        return "low"

    def addSender(self, name):
        self.input_modules[name] = "low"
        return


# start by building all of the objects we will need
modules = {}
for line in L:
    new_mod = line.split(" -> ")[0]
    if new_mod[0] == "b":
        continue

    symbol = new_mod[0]
    name = new_mod[1:]
    if symbol == "%":
        modules[name] = FlipFlop(name)
    else:
        modules[name] = Conjunction(name)


# iterate to complete the construction of the conjunctions
for line in L:
    if line[0] != "%":
        continue

    src = line.replace("%", "").split(" -> ")[0]
    dests = line.split(" -> ")[1].split(", ")
    for dest in dests:
        if isinstance(modules[dest], Conjunction):
            modules[dest].addSender(src)


# now build the instruction set
instructions = {}
for line in L:
    line = line.replace("%", "")
    line = line.replace("&", "")

    src, dest = line.split(" -> ")
    dest_array = dest.split(", ")
    instructions[src] = dest_array


# build the functino to press buttons
lows = 0
highs = 0


def press_button():
    global lows
    global highs

    lows += 1
    q = []

    # initialize by sending a low pulse via broadcaster
    for dest in instructions["broadcaster"]:
        q.append([dest, "low", "broadcaster"])

    # now keep going until the queue is done
    while len(q) > 0:
        instruction = q.pop(0)
        dest, pulse, src = instruction
        # print(f"{src} sent {pulse} to {dest}")

        if instruction[1] == "high":
            highs += 1
        else:
            lows += 1

        # figure out what we are going to send
        module = modules.get(instruction[0], None)
        if not module:
            continue

        if isinstance(module, FlipFlop):
            response = module.handlePulse(instruction[1])
        else:
            response = module.handlePulse(instruction[2], instruction[1])

        if response == "":
            continue

        # now send out the response to each of the destinations
        destinations = instructions[instruction[0]]
        for dest in destinations:
            q.append([dest, response, instruction[0]])


for i in range(int(button_presses)):
    press_button()

print(f"Highs: {highs}")
print(f"Lows: {lows}")
print(f"Ans: {highs * lows}")
