import sys
import math

input_filepath = sys.argv[1]

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

    def allHigh(self):
        for pulse in self.input_modules.values():
            if pulse != "high":
                return False
        return True


modules = {}
instructions = {}


def reset():
    global modules
    global instructions

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


def press_button(conj):
    global lows
    global highs

    q = []

    # initialize by sending a low pulse via broadcaster
    for dest in instructions["broadcaster"]:
        q.append([dest, "low", "broadcaster"])

    # now keep going until the queue is done
    while len(q) > 0:
        instruction = q.pop(0)
        dest, pulse, src = instruction
        # print(f"{src} sent {pulse} to {dest}")

        # check if we met our condition
        if src == conj and pulse == "high":
            return True

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

    return False


reset()
# find all of the modules that feed into rx
rx_sources = []
for src, dests in instructions.items():
    if "rx" in dests:
        rx_sources.append(src)

# for each rx source, find the sources
conjs_to_solve = []
for src in rx_sources:
    for sender, dests in instructions.items():
        if src in dests:
            conjs_to_solve.append(sender)
print(conjs_to_solve)

multiples = []
for conj in conjs_to_solve:
    reset()
    presses = 1
    while not press_button(conj):
        presses += 1
        if presses % 100000 == 0:
            print(presses)

    multiples.append(presses)

print(multiples)
print(math.lcm(*multiples))
